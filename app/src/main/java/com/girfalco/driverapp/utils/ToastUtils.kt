package com.girfalco.driverapp.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.girfalco.driverapp.R

enum class ToastType {
    SUCCESS,
    ERROR
}

object ToastUtils {

    fun showCustomToast(context: Context, message: String, type: ToastType) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.layout_custom_toast, null)

        val toastIcon = layout.findViewById<ImageView>(R.id.toast_icon)
        val toastText = layout.findViewById<TextView>(R.id.toast_text)
        val toastRoot = layout.findViewById<LinearLayout>(R.id.toast_root)

        toastText.text = message

        when (type) {
            ToastType.SUCCESS -> {
                toastRoot.setBackgroundResource(R.drawable.bg_toast_success)
                toastIcon.setImageResource(R.drawable.tosst_ok_tick)
            }
            ToastType.ERROR -> {
                toastRoot.setBackgroundResource(R.drawable.bg_toast_error)
                toastIcon.setImageResource(R.drawable.ic_error_icon) // Assuming you have an error icon
            }
        }

        with(Toast(context)) {
            setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 20)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
}
