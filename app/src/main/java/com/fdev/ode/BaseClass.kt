package com.fdev.ode

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView


class BaseClass {

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null)
            return true

        return false
    }

    fun setNameView(view: TextView, text: String, sizeWidth: Double, font: Typeface) {
        view.textSize = 25f
        view.text = text
        view.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
        )
        view.gravity = Gravity.START
        view.typeface = font
        view.translationZ = 30F
        view.setTextColor(Color.WHITE)
        setMargins(
            view,
            (sizeWidth * .05).toInt(),
            0, 0, 0
        )
    }

    fun setDateView(view: TextView, text: String, font: Typeface) {
        view.textSize = 14f
        view.text = text
        view.setTextColor(Color.WHITE)
        view.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
        )
        view.gravity = Gravity.END
        view.typeface = font
        view.translationZ = 18F
    }

    fun setLabelView(view: TextView, text: String, sizeHeight: Int, font: Typeface) {
        view.textSize = 20f
        view.text = "\"${text}\""
        view.setTextColor(Color.WHITE)
        view.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
        )
        view.gravity = Gravity.CENTER
        view.typeface = font
        view.translationZ = 18F
        setMargins(
            view,
            0,
            sizeHeight, 0, 0
        )
    }

    fun setAmountView(view: TextView, text: String, sizeHeight: Int, font: Typeface) {
        view.textSize = 30f
        view.text = text
        view.gravity = Gravity.CENTER
        view.typeface = font
        view.setTextColor(Color.WHITE)
        view.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
        )
        setMargins(view, 0, sizeHeight, 0, 0)
    }

    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = v.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }

    fun getScreenWidth(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun getScreenHeight(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun disableViews(view: List<View>) {
        for (i in view.indices)
            view[i].isEnabled = false
    }

    fun enableViews(view: List<View>) {
        for (i in view.indices)
            view[i].isEnabled = true
    }

}