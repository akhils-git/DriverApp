package com.girfalco.driverapp


import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog

class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // Hide status bar and navigation bar for fullscreen
        window.decorView.systemUiVisibility = (
            android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
        )

        // Set up back arrow click to finish activity
        val backArrow = findViewById<android.widget.ImageView>(R.id.user_profile_back_arrow)
        backArrow?.setOnClickListener {
            finish()
        }

        // Set up logout button click to show confirmation popup
        val logoutText = findViewById<TextView>(R.id.logout_text)
        logoutText?.setOnClickListener {
            showLogoutConfirmation()
        }

        // Set up edit profile button click to show edit profile picture popup
        val editProfileButton = findViewById<android.widget.ImageView>(R.id.user_profile_edit)
        editProfileButton?.setOnClickListener {
            showEditProfilePicturePopup()
        }
    }

    private fun showEditProfilePicturePopup() {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.edit_profile_picture_bottom_sheet, null)
        dialog.setContentView(view)

        // Hide navigation bar when popup is shown
        val originalUiFlags = this@UserProfileActivity.window.decorView.systemUiVisibility
        dialog.setOnShowListener {
            val flags = (
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
            )
            this@UserProfileActivity.window.decorView.systemUiVisibility = flags
            dialog.window?.decorView?.systemUiVisibility = flags
            dialog.window?.setFlags(
                android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        dialog.setOnDismissListener {
            this@UserProfileActivity.window.decorView.systemUiVisibility = originalUiFlags
        }

        // Make the bottom sheet background transparent and avoid black nav bar
        val bottomSheet = dialog.delegate.findViewById<android.view.View>(com.google.android.material.R.id.design_bottom_sheet)
        val dialogWindow = dialog.window
        if (dialogWindow != null) {
            dialogWindow.navigationBarColor = android.graphics.Color.TRANSPARENT
            dialogWindow.setBackgroundDrawableResource(android.R.color.transparent)
        }

        // Set text and icon for Choose Photo button
        val choosePhotoButton = view.findViewById<android.view.View>(R.id.popup_action_choose_photo)
        choosePhotoButton.findViewById<TextView>(R.id.popup_action_text).text = "Choose Photo"
        choosePhotoButton.findViewById<ImageView>(R.id.popup_action_icon).setImageResource(R.drawable.edit_pop_up_choose_photo)

        // Set text and icon for Delete Photo button
        val deletePhotoButton = view.findViewById<android.view.View>(R.id.popup_action_delete_photo)
        deletePhotoButton.findViewById<TextView>(R.id.popup_action_text).text = "Delete Photo"
        deletePhotoButton.findViewById<ImageView>(R.id.popup_action_icon).setImageResource(R.drawable.edit_pop_up_delete_photo)

        dialog.show()
    }

    private fun showLogoutConfirmation() {
        val dialog = BottomSheetDialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.logout_bottom_sheet, null)
        dialog.setContentView(view)

        // Hide navigation bar when popup is shown
        val originalUiFlags = window.decorView.systemUiVisibility
        dialog.setOnShowListener {
            // Set immersive flags on both activity and dialog
            val flags = (
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
            )
            window.decorView.systemUiVisibility = flags
            dialog.window?.decorView?.systemUiVisibility = flags
            dialog.window?.setFlags(
                android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        dialog.setOnDismissListener {
            window.decorView.systemUiVisibility = originalUiFlags
        }

        // Make the bottom sheet background transparent and avoid black nav bar
        val bottomSheet = dialog.delegate.findViewById<android.view.View>(com.google.android.material.R.id.design_bottom_sheet)
        val window = dialog.window
        if (window != null) {
            window.navigationBarColor = android.graphics.Color.TRANSPARENT
            window.setBackgroundDrawableResource(android.R.color.transparent)
        }

        // Set Cancel button to dismiss the dialog
        val cancelButton = view.findViewById<android.widget.Button>(R.id.cancel_button)
        cancelButton?.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}