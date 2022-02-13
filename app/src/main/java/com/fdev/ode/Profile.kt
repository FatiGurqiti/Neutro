package com.fdev.ode

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.AnyRes
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Profile : AppCompatActivity() {

    private var ono: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        ono="gotttcha"

        var username = findViewById<TextView>(R.id.username)
        var usermail = findViewById<TextView>(R.id.userMail)
        var odeno = findViewById<TextView>(R.id.userOdeNo)
        val logo = findViewById<ImageView>(R.id.logoinProfile)
        val copybtn = findViewById<ImageButton>(R.id.copyBtn)

        val user = Firebase.auth.currentUser


        if (user != null) {
            usermail.text = user.email
        }
        if (user != null) {
            odeno.text=user.uid
        }


        logo.setOnClickListener() {
            Toast.makeText(this, "Cleveeer", Toast.LENGTH_SHORT).show()
            copyText()
        }

        copybtn.setOnClickListener() {
            copyText()
            Toast.makeText(this, "Öde number copied to clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    fun copyText(){
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", ono)
        clipboardManager.setPrimaryClip(clipData)
    }
}