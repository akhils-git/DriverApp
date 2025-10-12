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
}
