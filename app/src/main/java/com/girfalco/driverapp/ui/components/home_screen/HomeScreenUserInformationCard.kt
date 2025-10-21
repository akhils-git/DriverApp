package com.girfalco.driverapp.ui.components.home_screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.RelativeLayout
import com.girfalco.driverapp.R
import com.girfalco.driverapp.UserProfileActivity
import android.widget.TextView

class HomeScreenUserInformationCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.homescreen_user_information_card, this, true)
        
        val profilePicture = findViewById<ImageView>(R.id.profile_picture)
        profilePicture.setOnClickListener {
            val intent = Intent(context, UserProfileActivity::class.java)
            context.startActivity(intent)
            if (context is Activity) {
                context.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        }
    }

    /**
     * Public setter to update the greeting text inside the card.
     * Pass null to reset to default string in layout.
     */
    fun setGreeting(greeting: String?) {
        val greetingText = findViewById<TextView>(R.id.greeting_text)
        if (greeting.isNullOrBlank()) {
            // let the layout default remain
            // Optionally you could set a fallback string here
            return
        }
        greetingText?.text = greeting
    }
}
