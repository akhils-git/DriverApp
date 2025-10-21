package com.girfalco.driverapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.girfalco.driverapp.model.Person
import com.girfalco.driverapp.model.PersonStore
import com.girfalco.driverapp.network.RetrofitProvider
import com.girfalco.driverapp.network.model.LoginResponse
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Parse LOGIN_RESPONSE_JSON (if present)
        val json = intent.getStringExtra("LOGIN_RESPONSE_JSON")
        val loginResponse: LoginResponse? = try {
            if (!json.isNullOrBlank()) Json.decodeFromString<LoginResponse>(json) else null
        } catch (e: Exception) {
            Log.w("HomeActivity", "Failed to parse LOGIN_RESPONSE_JSON", e)
            null
        }

        val success = loginResponse?.success ?: false
        val message = loginResponse?.message
        val token = loginResponse?.token
        val userId = loginResponse?.userId

        val fallbackMessage: String? = intent.getStringExtra("LOGIN_SUCCESS_MESSAGE")
        val finalMessage = fallbackMessage ?: message ?: "Login Successful"

        Log.d("LOGIN", "success=$success message=$message token=$token userId=$userId")

        val userCard = findViewById<com.girfalco.driverapp.ui.components.home_screen.HomeScreenUserInformationCard>(R.id.text_container)
        if (userCard != null) {
            userCard.setGreeting(finalMessage)
        } else {
            findViewById<TextView>(R.id.greeting_text)?.text = finalMessage
        }

        // If a success message was passed from Login, keep a log entry
        fallbackMessage?.let { msg ->
            Log.d("HomeActivity", "Login success message received: $msg")
        }

        // Fetch person details if we have an id
        val idInt = userId?.toIntOrNull()
        if (idInt != null) {
            lifecycleScope.launch {
                try {
                    val resp = RetrofitProvider.personApi.getPersonById(idInt)
                    val personResult = resp.results.firstOrNull()
                    if (personResult != null) {
                        val person = Person(
                            id = personResult.ID,
                            firstName = personResult.FirstName,
                            lastName = personResult.LastName,
                            email = personResult.Email,
                            mobile = personResult.Mobile,
                            image = personResult.Image
                        )
                        PersonStore.current = person
                        val displayName = listOfNotNull(person.firstName, person.lastName).joinToString(" ").ifBlank { null }
                        if (!displayName.isNullOrBlank()) {
                            userCard?.setUserName(displayName)
                        }
                    }
                } catch (e: Exception) {
                    Log.w("HomeActivity", "Failed to load person by id=$idInt", e)
                }
            }
        }

        // Update UI from store if already populated
        updateUserNameFromStore()
    }

    override fun onResume() {
        super.onResume()
        updateUserNameFromStore()
    }

    private fun updateUserNameFromStore() {
        val userCard = findViewById<com.girfalco.driverapp.ui.components.home_screen.HomeScreenUserInformationCard>(R.id.text_container)
        val person = PersonStore.current
        val displayName = person?.let { listOfNotNull(it.firstName, it.lastName).joinToString(" ").ifBlank { null } }
        if (!displayName.isNullOrBlank()) {
            userCard?.setUserName(displayName)
        }
    }

    fun showSelectVehiclePopup() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.select_vehicle_bottom_sheet, null)
        dialog.setContentView(view)

        // Force popup height to 640dp
        view.post {
            val bottomSheet = dialog.delegate.findViewById<android.view.View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val params = it.layoutParams
                val density = resources.displayMetrics.density
                params.height = (640 * density).toInt()
                it.layoutParams = params
            }
        }

        dialog.show()
    }

    private fun showFilterPopup() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.filter_popup, null)
        dialog.setContentView(view)
        dialog.show()
    }
}

package com.girfalco.driverapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.girfalco.driverapp.network.model.LoginResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import android.view.View
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Button
import android.content.Intent
import androidx.lifecycle.lifecycleScope
    
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        )

        // Set status bar color to white to match user card
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = android.graphics.Color.WHITE
            window.navigationBarColor = android.graphics.Color.parseColor("#FFF9E5")
        }

        // Hide bottom navigation bar (immersive sticky mode)
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
        )

        // Find the filter icon ImageView
        val filterIcon = findViewById<ImageView>(R.id.home_screen_filter)
        filterIcon?.setOnClickListener { v ->
            v.animate()
                .scaleX(0.93f)
                .scaleY(0.93f)
                .setDuration(80)
                .withEndAction {
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(80)
                        .withEndAction {
                            showFilterPopup()
                        }
                        .start()
                }
                .start()
        }

        // Set up Change button to show SelectVehicle popup
        val vehicleCard = findViewById<com.girfalco.driverapp.ui.components.home_screen.VehicleInformationCard>(R.id.vehicle_card)
        vehicleCard?.onChangeButtonClick = {
            showSelectVehiclePopup()
        }

        // Add scale animation to View Details button (first instance only)
        val currentLocationDetails1 = findViewById<View>(R.id.current_location_details_include_1)
        val viewDetailsButtonRow1 = currentLocationDetails1?.findViewById<View>(R.id.view_details_button_row)
        viewDetailsButtonRow1?.setOnClickListener { v ->
            v.animate()
                .scaleX(0.93f)
                .scaleY(0.93f)
                .setDuration(80)
                .withEndAction {
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(80)
                        .withEndAction {
                            // Navigate to Route Details screen
                            val intent = Intent(this, RouteDetailsActivity::class.java)
                            startActivity(intent)
                        }
                        .start()
                }
                .start()
        }

        // Immediately update UI from any stored person (if already loaded)
        updateUserNameFromStore()
    }

    override fun onResume() {
        super.onResume()
        // Refresh the displayed name from the store in case it was populated elsewhere
        updateUserNameFromStore()
    }

    private fun updateUserNameFromStore() {
        val userCard = findViewById<com.girfalco.driverapp.ui.components.home_screen.HomeScreenUserInformationCard>(R.id.text_container)
        val person = com.girfalco.driverapp.model.PersonStore.current
        val displayName = person?.let { listOfNotNull(it.firstName, it.lastName).joinToString(" ").ifBlank { null } }
        if (!displayName.isNullOrBlank()) {
            userCard?.setUserName(displayName)
        }
    }
        // If a success message was passed from Login, keep a log entry (popup shown by LoginFragment)
        intent?.getStringExtra("LOGIN_SUCCESS_MESSAGE")?.let { msg ->
            Log.d("HomeActivity", "Login success message received: $msg")
            // Note: showSuccessPopup is invoked from LoginFragment before navigation; removing here to avoid build errors
        }

        // Hide the navigation bar and status bar for immersive fullscreen experience
        window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    package com.girfalco.driverapp

                    import android.os.Bundle
                    import android.util.Log
                    import android.widget.TextView
                    import com.girfalco.driverapp.network.model.LoginResponse
                    import kotlinx.serialization.decodeFromString
                    import kotlinx.serialization.json.Json
                    import android.view.View
                    import android.view.LayoutInflater
                    import android.widget.ImageView
                    import androidx.lifecycle.lifecycleScope
                    import com.girfalco.driverapp.model.Person
                    import com.girfalco.driverapp.model.PersonStore
                    import com.girfalco.driverapp.network.RetrofitProvider
                    import kotlinx.coroutines.launch
                    import androidx.appcompat.app.AppCompatActivity

                    class HomeActivity : AppCompatActivity() {
                        override fun onCreate(savedInstanceState: Bundle?) {
                            super.onCreate(savedInstanceState)
                            setContentView(R.layout.activity_home)

                            // Parse LOGIN_RESPONSE_JSON (if present) and populate greeting text
                            val json = intent.getStringExtra("LOGIN_RESPONSE_JSON")

                            val loginResponse: LoginResponse? = try {
                                if (!json.isNullOrBlank()) Json.decodeFromString<LoginResponse>(json) else null
                            } catch (e: Exception) {
                                Log.w("HomeActivity", "Failed to parse LOGIN_RESPONSE_JSON", e)
                                null
                            }

                            // Access all values (null-safe)
                            val success: Boolean = loginResponse?.success ?: false
                            val message: String? = loginResponse?.message
                            val token: String? = loginResponse?.token
                            val userId: String? = loginResponse?.userId

                            // Optional: fallback to the explicit extra that LoginFragment sometimes sets
                            val fallbackMessage: String? = intent.getStringExtra("LOGIN_SUCCESS_MESSAGE")
                            val finalMessage = fallbackMessage ?: message ?: "Login Successful"

                            // Log and populate greeting in the custom user information card (if present)
                            Log.d("LOGIN", "success=$success message=$message token=$token userId=$userId")
                            val userCard = findViewById<com.girfalco.driverapp.ui.components.home_screen.HomeScreenUserInformationCard>(R.id.text_container)
                            if (userCard != null) {
                                userCard.setGreeting(finalMessage)
                            } else {
                                // fallback: try to set the plain TextView
                                findViewById<TextView>(R.id.greeting_text)?.text = finalMessage
                            }

                            // Fetch full person details from the API when we have a userId
                            val idInt = userId?.toIntOrNull()
                            if (idInt != null) {
                                lifecycleScope.launch {
                                    try {
                                        val resp = RetrofitProvider.personApi.getPersonById(idInt)
                                        val personResult = resp.results.firstOrNull()
                                        if (personResult != null) {
                                            val person = Person(
                                                id = personResult.ID,
                                                firstName = personResult.FirstName,
                                                lastName = personResult.LastName,
                                                email = personResult.Email,
                                                mobile = personResult.Mobile,
                                                image = personResult.Image
                                            )
                                            PersonStore.current = person

                                            // update UI on main thread
                                            val displayName = listOfNotNull(person.firstName, person.lastName).joinToString(" ").ifBlank { null }
                                            if (displayName != null) {
                                                userCard?.setUserName(displayName)
                                            }
                                        }
                                    } catch (e: Exception) {
                                        Log.w("HomeActivity", "Failed to load person by id=$idInt", e)
                                    }
                                }
                            }

                            // Immediately update UI from any stored person (if already loaded)
                            updateUserNameFromStore()
                        }

                        override fun onResume() {
                            super.onResume()
                            // Refresh the displayed name from the store in case it was populated elsewhere
                            updateUserNameFromStore()
                        }

                        private fun updateUserNameFromStore() {
                            val userCard = findViewById<com.girfalco.driverapp.ui.components.home_screen.HomeScreenUserInformationCard>(R.id.text_container)
                            val person = com.girfalco.driverapp.model.PersonStore.current
                            val displayName = person?.let { listOfNotNull(it.firstName, it.lastName).joinToString(" ").ifBlank { null } }
                            if (!displayName.isNullOrBlank()) {
                                userCard?.setUserName(displayName)
                            }
                        }

                        fun showSelectVehiclePopup() {

                            val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
                            val view = LayoutInflater.from(this).inflate(R.layout.select_vehicle_bottom_sheet, null)
                            dialog.setContentView(view)

                            // Force popup height to 640dp
                            view.post {
                                val bottomSheet = dialog.delegate.findViewById<android.view.View>(com.google.android.material.R.id.design_bottom_sheet)
                                bottomSheet?.let {
                                    val params = it.layoutParams
                                    val density = resources.displayMetrics.density
                                    params.height = (640 * density).toInt()
                                    it.layoutParams = params
                                }
                            }

                            dialog.show()
                        }

                        private fun showFilterPopup() {
                            val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
                            val view = LayoutInflater.from(this).inflate(R.layout.filter_popup, null)
                            dialog.setContentView(view)

                            dialog.show()
                        }
                    }
