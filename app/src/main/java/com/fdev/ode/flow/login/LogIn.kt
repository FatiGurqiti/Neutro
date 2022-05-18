package com.fdev.ode.flow.login

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fdev.ode.*
import com.fdev.ode.flow.main.MainActivity
import com.fdev.ode.flow.signup.SignUp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*


class LogIn : AppCompatActivity() {

    private val baseClass = BaseClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        val user = Firebase.auth.currentUser

        if (user != null)
            loginEmail.setText(user.email.toString())

        logoinBtn.setOnClickListener {
            progressBarInLogIn.visibility = View.VISIBLE

            if (canClick()) {
                viewModel.signIn(
                    loginEmail.text.toString(),
                    loginPassword.text.toString(),
                    this
                )
                viewModel.canLogin.observe(this) {
                    if (it) goto(MainActivity::class.java)
                }
            }
            progressBarInLogIn.visibility = View.INVISIBLE
        }


        forgotPassword.setOnClickListener()
        {
            goto(forgotPassword::class.java)
        }

        signUpTxt.setOnClickListener {
            goto(SignUp::class.java)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        progressBarInLogIn.visibility = View.INVISIBLE
    }

    private fun canClick(): Boolean {
        var value = false
        if (baseClass.isOnline(applicationContext)) {
            val emailTxt = loginEmail.text.toString()
            val pinTxt = loginPassword.text.toString()

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