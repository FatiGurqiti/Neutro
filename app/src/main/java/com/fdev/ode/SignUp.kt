package com.fdev.ode

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val progressBar = findViewById<ProgressBar>(R.id.progressBarinSignUp)

        //Open Login Activity
        val LoginTxt = findViewById<TextView>(R.id.loginTxt)
        LoginTxt.setOnClickListener{
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        //SignUp
        val SignUpButton = findViewById<ImageButton>(R.id.SignBtn)
        SignUpButton.setOnClickListener(){
            progressBar.visibility = View.VISIBLE
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
            progressBar.visibility = View.INVISIBLE
        }

    }
}