package com.girfalco.driverapp.ui.components.home_screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import coil.load
import com.girfalco.driverapp.R
import com.girfalco.driverapp.UserProfileActivity
import android.widget.TextView

class HomeScreenUserInformationCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val profilePicture: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.homescreen_user_information_card, this, true)
        
        profilePicture = findViewById(R.id.profile_picture)
        profilePicture.setOnClickListener {
            val intent = Intent(context, UserProfileActivity::class.java)
            context.startActivity(intent)
            if (context is Activity) {
                context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    fun setGreeting(greeting: String?) {
        val greetingText = findViewById<TextView>(R.id.greeting_text)
        if (greeting.isNullOrBlank()) {
            return
        }
        greetingText?.text = greeting
    }

    fun setUserName(name: String?) {
        val nameText = findViewById<TextView>(R.id.user_name_text)
        if (name.isNullOrBlank()) return
        nameText?.text = name
    }

    fun setUserImage(imagePath: String?) {
        val imageUrl = if (imagePath != null) "https://api.girfalco.sa/uploads/person/$imagePath" else null

        // Add the following dependency to your app-level build.gradle file:
        // implementation("io.coil-kt:coil:2.4.0")
        profilePicture.load(imageUrl) {
            placeholder(R.drawable.user_avathar) // Corrected placeholder
            error(R.drawable.user_avathar)       // Corrected error drawable
        }
    }
}
