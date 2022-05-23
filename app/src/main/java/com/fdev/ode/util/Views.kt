package com.fdev.ode.util

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.RelativeLayout
import android.widget.TextView

class Views : BaseClass() {


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
}