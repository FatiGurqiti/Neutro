package com.fdev.ode

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        var username = findViewById<TextView>(R.id.username)
        var usermail = findViewById<TextView>(R.id.userMail)
        val logo = findViewById<ImageView>(R.id.logoinProfile)
        val scrollLayout = findViewById<RelativeLayout>(R.id.Scroll_Relative)
        val user = Firebase.auth.currentUser

        if (user != null) {
            usermail.text = user.email
        }

        var sizeheight = getScreenHeight(this) * 0.5
        var sizewidth = getScreenWidth(this)

        val face = resources.getFont(R.font.plusjakartatextregular)
        val boldface = resources.getFont(R.font.plusjakartatexbold)

        val Contact_Name = TextView(this)
        Contact_Name.textSize = 20f
        Contact_Name.text = "Fati"
        Contact_Name.setTypeface(boldface)
        scrollLayout.addView(Contact_Name)
        setMargins(Contact_Name, (sizewidth * 0.1).toInt(),  ( (sizeheight) * 0.2).toInt(), 25, 1);

        val Contact_Mail = TextView(this)
        Contact_Mail.textSize = 20f
        Contact_Mail.text = "fatigurqti@gmail.com"
        Contact_Mail.setTypeface(face)
        scrollLayout.addView(Contact_Mail)
        setMargins(Contact_Mail, (sizewidth * 0.1).toInt(),  ( (sizeheight) * 0.3).toInt(), 25, 1);

        logo.setOnClickListener() {
            Toast.makeText(this, "Cleveeer", Toast.LENGTH_SHORT).show()
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", user?.email.toString())
            clipboardManager.setPrimaryClip(clipData)
        }

    }


    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.getLayoutParams() is MarginLayoutParams) {
            val p = v.getLayoutParams() as MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }

    fun getScreenHeight(context: Context?): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
    fun getScreenWidth(context: Context?): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

}