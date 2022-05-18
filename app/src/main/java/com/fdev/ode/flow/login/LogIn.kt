package com.fdev.ode.flow.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fdev.ode.*
import com.fdev.ode.flow.forgotPassword.ForgotPassword
import com.fdev.ode.flow.main.MainActivity
import com.fdev.ode.flow.signup.SignUp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LogIn : AppCompatActivity() {

    private val baseClass = BaseClass()
    private lateinit var progressBar: ProgressBar
    private lateinit var email: EditText
    private lateinit var pin: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val user = Firebase.auth.currentUser
        val signUpBtn = findViewById<TextView>(R.id.signUpTxt)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)
        val login = findViewById<ImageButton>(R.id.logoinBtn)
        progressBar = findViewById(R.id.progressBarInLogIn)
        email = findViewById(R.id.loginEmail)
        pin = findViewById(R.id.loginPassword)

        if (user != null)
            email.setText(user.email.toString())

        login.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            if (canClick()) {
                viewModel.signIn(
                    email.text.toString(),
                    pin.text.toString(),
                    this
                )
                viewModel.canLogin.observe(this, Observer {
                    if (it) goto(MainActivity::class.java)
                })
            }
            progressBar.visibility = View.INVISIBLE
        }


        forgotPassword.setOnClickListener()
        {
            goto(ForgotPassword::class.java)
        }

        signUpBtn.setOnClickListener {
            goto(SignUp::class.java)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        progressBar.visibility = View.INVISIBLE
    }

    private fun canClick(): Boolean {
        var value = false
        if (baseClass.isOnline(applicationContext)) {
            val emailTxt = email.text.toString()
            val pinTxt = pin.text.toString()

            if (TextUtils.isEmpty(emailTxt) || TextUtils.isEmpty(pinTxt) || emailTxt.length < 6)
                Toast.makeText(this, "Mind if you fill the inputs?", Toast.LENGTH_SHORT).show()
            else value = true
        } else
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT)
                .show()

        return value
    }

    private fun goto(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }
}