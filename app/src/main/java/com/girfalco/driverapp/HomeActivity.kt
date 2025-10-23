package com.girfalco.driverapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.girfalco.driverapp.network.model.LoginResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.girfalco.driverapp.model.Person
import com.girfalco.driverapp.model.PersonStore
import com.girfalco.driverapp.network.RetrofitProvider
import kotlinx.coroutines.launch
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.girfalco.driverapp.ui.components.home_screen.VehicleAdapter
import com.girfalco.driverapp.network.model.Vehicle
import com.girfalco.driverapp.ui.components.home_screen.VehicleInformationCard

class HomeActivity : AppCompatActivity() {

    // Re-usable controller for hiding/showing system bars
    private lateinit var insetsController: WindowInsetsControllerCompat

    // List of vehicles for the current company (populated after API call)
    private var vehicleList: List<Vehicle> = emptyList()

    // Currently selected vehicle for the driver
    private var selectedVehicle: Vehicle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Parse LOGIN_RESPONSE_JSON (if present) and populate greeting text
        val json = intent.getStringExtra("LOGIN_RESPONSE_JSON")

        // Use a Json instance that ignores unknown keys so server extras won't break deserialization
        val deserializer = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
        val loginResponse: LoginResponse? = try {
            if (!json.isNullOrBlank()) deserializer.decodeFromString<LoginResponse>(json) else null
        } catch (e: Exception) {
            Log.w("HomeActivity", "Failed to parse LOGIN_RESPONSE_JSON", e)
            null
        }

        // Access all values (null-safe)
        val success: Boolean = loginResponse?.success ?: false
        val message: String? = loginResponse?.message
        val token: String? = loginResponse?.token
        val userId: String? = loginResponse?.userId

        // Store the auth token for subsequent API calls
        if (token != null) {
            com.girfalco.driverapp.network.AuthTokenStore.token = token
        }

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

        // Initialize vehicleCard here so it's accessible within the coroutine scope
        val vehicleCard = findViewById<com.girfalco.driverapp.ui.components.home_screen.VehicleInformationCard>(R.id.vehicle_card)

        // Fetch full person details from the API when we have a userId
        val idInt = userId?.toIntOrNull()
        if (idInt != null) {
            lifecycleScope.launch {
                try {
                    // Debug: log token and id just before making the request
                    val currentToken = com.girfalco.driverapp.network.AuthTokenStore.token
                    Log.d("HomeActivity", "calling getPersonById id=$idInt tokenPresent=${!currentToken.isNullOrBlank()}")
                    val response = RetrofitProvider.personApi.getPersonById(idInt)
                    Log.d("HomeActivity", "getPersonById HTTP code=${response.code()} success=${response.isSuccessful}")

                    if (response.isSuccessful) {
                        val respBody = response.body()
                        Log.d("HomeActivity", "getPersonById body: $respBody")
                        val personResult = respBody?.results?.firstOrNull()
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

                            // --- NEW: Call Vehicle list API for this person's company ---
                            val companyId = personResult.CompanyID
                            if (companyId != null) {
                                try {
                                    Log.d("HomeActivity", "calling listCompanyVehicles companyId=$companyId tokenPresent=${!currentToken.isNullOrBlank()}")
                                    val vehicleResp = RetrofitProvider.vehicleApi.listCompanyVehicles(companyId)
                                    Log.d("HomeActivity", "listCompanyVehicles HTTP code=${vehicleResp.code()} success=${vehicleResp.isSuccessful}")
                                    if (vehicleResp.isSuccessful) {
                                        val vehiclesBody = vehicleResp.body()
                                        vehicleList = vehiclesBody?.data ?: emptyList()
                                        Log.d("HomeActivity", "Loaded vehicles count=${vehicleList.size}")
                                        // Automatically select the first vehicle if available
                                        selectedVehicle = vehicleList.firstOrNull()
                                        vehicleCard?.setVehicleDetails(selectedVehicle?.NumberPlate, selectedVehicle?.Name, selectedVehicle?.Make, selectedVehicle?.Model)
                                    } else {
                                        val err = try { vehicleResp.errorBody()?.string() } catch (_: Exception) { null }
                                        Log.w("HomeActivity", "listCompanyVehicles failed: code=${vehicleResp.code()} error=$err")
                                    }
                                } catch (e: Exception) {
                                    Log.w("HomeActivity", "Failed to load vehicles for companyId=$companyId", e)
                                }
                            } else {
                                Log.w("HomeActivity", "No CompanyID in person result; skipping vehicle list fetch")
                            }

                        } else {
                            Log.w("HomeActivity", "getPersonById: no results in body")
                        }
                    } else {
                        val err = try { response.errorBody()?.string() } catch (_: Exception) { null }
                        Log.w("HomeActivity", "getPersonById failed: code=${response.code()} error=$err")
                    }
                } catch (e: Exception) {
                    Log.w("HomeActivity", "Failed to load person by id=$idInt", e)
                }
            }
        }


        // If a success message was passed from Login, keep a log entry (popup shown by LoginFragment)
        intent?.getStringExtra("LOGIN_SUCCESS_MESSAGE")?.let { msg ->
            Log.d("HomeActivity", "Login success message received: $msg")
        }

        // --- Modern immersive/fullscreen handling (replace deprecated systemUiVisibility) ---
        WindowCompat.setDecorFitsSystemWindows(window, false)
        insetsController = WindowInsetsControllerCompat(window, window.decorView)
        // Allow transient system bars to be revealed with a swipe
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both status and navigation bars for immersive experience
        insetsController.hide(WindowInsetsCompat.Type.systemBars())

        // Set status and navigation bar colors (still fine to set directly)
        // minSdk is 24 so the LOLLIPOP check is unnecessary; kept for clarity
        window.statusBarColor = android.graphics.Color.WHITE
        window.navigationBarColor = "#FFF9E5".toColorInt()

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
    }

    fun showSelectVehiclePopup() {

        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val parent = findViewById<ViewGroup>(android.R.id.content)
        val view = LayoutInflater.from(this).inflate(R.layout.select_vehicle_bottom_sheet, parent, false)
        dialog.setContentView(view)

        // Force popup height to 640dp
        view.post {
            val bottomSheet = dialog.delegate.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val params = it.layoutParams
                val density = resources.displayMetrics.density
                params.height = (640 * density).toInt()
                it.layoutParams = params
            }
        }

        // Use WindowInsetsControllerCompat to hide system bars when dialog is shown
        dialog.setOnShowListener {
            // Hide system bars for dialog window as well
            dialog.window?.let { win ->
                val dlgCtrl = WindowInsetsControllerCompat(win, win.decorView)
                dlgCtrl.hide(WindowInsetsCompat.Type.systemBars())
                win.setFlags(
                    android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
                win.setBackgroundDrawableResource(android.R.color.transparent)
                val bottomSheet = dialog.delegate.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
        }
        dialog.setOnDismissListener {
            // Restore system bars on dismiss
            insetsController.show(WindowInsetsCompat.Type.systemBars())
        }

        val dialogWindow = dialog.window
        if (dialogWindow != null) {
            dialogWindow.navigationBarColor = android.graphics.Color.TRANSPARENT
            dialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
        }

        // --- RecyclerView setup for vehicles ---
        val recyclerView = view.findViewById<RecyclerView>(R.id.vehicle_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val vehicleAdapter = VehicleAdapter(vehicleList, selectedVehicle) {
            // Lambda for when a vehicle is selected in the adapter
            selectedVehicle = it
            val vehicleCard = findViewById<com.girfalco.driverapp.ui.components.home_screen.VehicleInformationCard>(R.id.vehicle_card)
            vehicleCard?.setVehicleDetails(selectedVehicle?.NumberPlate, selectedVehicle?.Name, selectedVehicle?.Make, selectedVehicle?.Model)
        }
        recyclerView.adapter = vehicleAdapter

        // "Choose This Vehicle" button click listener
        view.findViewById<View>(R.id.choose_vehicle_button_text)?.setOnClickListener { // Use the text view as the clickable area
            // You can add logic here to confirm the selection and potentially dismiss the dialog
            Log.d("HomeActivity", "Chosen vehicle: ${selectedVehicle?.NumberPlate}")
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showFilterPopup() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(this)
        val parent = findViewById<ViewGroup>(android.R.id.content)
        val view = LayoutInflater.from(this).inflate(R.layout.filter_popup, parent, false)
        dialog.setContentView(view)

        // Use WindowInsetsControllerCompat for dialog immersive flags
        dialog.setOnShowListener {
            dialog.window?.let { win ->
                val dlgCtrl = WindowInsetsControllerCompat(win, win.decorView)
                dlgCtrl.hide(WindowInsetsCompat.Type.systemBars())
                win.setFlags(
                    android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
                win.setBackgroundDrawableResource(android.R.color.transparent)
                val bottomSheet = dialog.delegate.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet?.setBackgroundColor(android.graphics.Color.TRANSPARENT)
            }
        }
        dialog.setOnDismissListener {
            insetsController.show(WindowInsetsCompat.Type.systemBars())
        }

        // Make the nav bar transparent
        val dialogWindow = dialog.window
        if (dialogWindow != null) {
            dialogWindow.navigationBarColor = android.graphics.Color.TRANSPARENT
            dialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
        }

        // Set text for each filter checkbox using resources
        val allRow = view.findViewById<View>(R.id.filter_option_all)
        val activeRow = view.findViewById<View>(R.id.filter_option_active)
        val notStartedRow = view.findViewById<View>(R.id.filter_option_not_started)
        val delayedRow = view.findViewById<View>(R.id.filter_option_delayed)

        allRow?.findViewById<TextView>(R.id.popup_action_text)?.text = getString(R.string.filter_all)
        activeRow?.findViewById<TextView>(R.id.popup_action_text)?.text = getString(R.string.filter_active_routes)
        notStartedRow?.findViewById<TextView>(R.id.popup_action_text)?.text = getString(R.string.filter_not_started)
        delayedRow?.findViewById<TextView>(R.id.popup_action_text)?.text = getString(R.string.filter_delayed_routes)

        // --- Make all checkboxes interactive ---
        val rows = listOf(allRow, activeRow, notStartedRow, delayedRow)
        val isCheckedList = mutableListOf(true, false, false, false)

        fun updateCheckbox(row: View?, checked: Boolean) {
            val checkbox = row?.findViewById<ImageView>(R.id.popup_action_checkbox)
            if (checkbox != null) {
                if (checked) {
                    checkbox.setImageResource(R.drawable.checkbox_checked)
                } else {
                    checkbox.setImageResource(R.drawable.checkbox_unchecked)
                }
            }
            row?.isSelected = checked
        }

        // Initialize all checkboxes
        for (i in rows.indices) {
            updateCheckbox(rows[i], isCheckedList[i])
        }

        // Set up toggle listeners for each row
        for (i in rows.indices) {
            val row = rows[i]
            val checkbox = row?.findViewById<ImageView>(R.id.popup_action_checkbox)
            val text = row?.findViewById<View>(R.id.popup_action_text)
            val toggleListener = View.OnClickListener {
                isCheckedList[i] = !isCheckedList[i]
                updateCheckbox(row, isCheckedList[i])
            }
            checkbox?.setOnClickListener(toggleListener)
            text?.setOnClickListener(toggleListener)
            row?.setOnClickListener(toggleListener)
        }

        // Set Apply button (LinearLayout) to dismiss the dialog
        view.findViewById<View>(R.id.apply_button)?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
