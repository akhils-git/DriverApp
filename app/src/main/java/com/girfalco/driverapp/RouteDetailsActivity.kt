package com.girfalco.driverapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RouteDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_details)

        // Set TabLayout tab text size to 14px for all tabs
        val tabLayout = findViewById<com.google.android.material.tabs.TabLayout>(R.id.route_tabs)
        tabLayout.post {
            for (i in 0 until tabLayout.tabCount) {
                val tab = tabLayout.getTabAt(i)
                if (tab != null) {
                    val context = tabLayout.context
                    val tabText = tab.text ?: ""
                    val tabView = android.widget.LinearLayout(context)
                    tabView.orientation = android.widget.LinearLayout.HORIZONTAL
                    tabView.gravity = android.view.Gravity.CENTER

                    val textView = android.widget.TextView(context)
                    textView.text = tabText
                    textView.setTextColor(if (i == tabLayout.selectedTabPosition) 0xFF2F3F46.toInt() else 0xFF4F5567.toInt())
                    textView.textSize = 14f
                    textView.typeface = if (i == tabLayout.selectedTabPosition) android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.BOLD) else android.graphics.Typeface.create("sans-serif-medium", android.graphics.Typeface.NORMAL)
                    tabView.addView(textView)

                    val rectContainer = android.widget.FrameLayout(context)
                    val params = android.widget.LinearLayout.LayoutParams(
                        (25 * context.resources.displayMetrics.density).toInt(),
                        (18 * context.resources.displayMetrics.density).toInt()
                    )
                    params.leftMargin = (5 * context.resources.displayMetrics.density).toInt()
                    params.gravity = android.view.Gravity.CENTER_VERTICAL
                    rectContainer.layoutParams = params
                    // Set rectangle background color based on tab
                    val rectColor = when (i) {
                        0 -> android.graphics.Color.parseColor("#DADCE2") // Upcoming
                        1 -> android.graphics.Color.parseColor("#FFDFDE") // Missed
                        2 -> android.graphics.Color.parseColor("#DDEDD5") // Completed
                        else -> android.graphics.Color.parseColor("#DADCE2")
                    }
                    val rectDrawable = android.graphics.drawable.GradientDrawable()
                    rectDrawable.setColor(rectColor)
                    rectDrawable.cornerRadius = 9 * context.resources.displayMetrics.density
                    rectContainer.background = rectDrawable

                    val numberText = android.widget.TextView(context)
                    numberText.text = "12"
                    numberText.setTextColor(0xFF2F3F46.toInt())
                    numberText.textSize = 13f
                    numberText.typeface = android.graphics.Typeface.create("sans-serif", android.graphics.Typeface.BOLD)
                    numberText.gravity = android.view.Gravity.CENTER
                    numberText.layoutParams = android.widget.FrameLayout.LayoutParams(
                        android.widget.FrameLayout.LayoutParams.MATCH_PARENT,
                        android.widget.FrameLayout.LayoutParams.MATCH_PARENT
                    )
                    rectContainer.addView(numberText)
                    tabView.addView(rectContainer)

                    tab.customView = tabView
                }
            }
        }

        // Hide status and navigation bars for immersive experience
        window.decorView.post {
            window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                        or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            )
        }

        // Back button logic with tap animation
        val backButton = findViewById<android.widget.ImageView>(R.id.route_details_back_arrow)
        backButton.setOnClickListener {
            backButton.animate().scaleX(0.85f).scaleY(0.85f).setDuration(80).withEndAction {
                backButton.animate().scaleX(1f).scaleY(1f).setDuration(80).withEndAction {
                    val intent = android.content.Intent(this, HomeActivity::class.java)
                    intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }.start()
            }.start()
        }
    }
}
