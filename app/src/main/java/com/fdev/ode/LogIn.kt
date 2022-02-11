package com.fdev.ode

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val progressBar = findViewById<ProgressBar>(R.id.progressBarInLogIn)
        val SignUpbtn = findViewById<TextView>(R.id.SignUpTxt)
        val LogIn = findViewById<ImageButton>(R.id.logoinBtn)



        SignUpbtn.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            progressBar.visibility = View.INVISIBLE
        }

        LogIn.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            progressBar.visibility = View.INVISIBLE
        }



    }
}