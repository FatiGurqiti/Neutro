package com.fdev.ode.flow.signup

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fdev.ode.BaseClass
import com.fdev.ode.R
import com.fdev.ode.flow.login.LogIn
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    private val baseClass = BaseClass()
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        signupBtn.setOnClickListener {
            signUpProgressBar.visibility = View.VISIBLE
            if (canClick(signUpUsernameEditText, signUpEmailEditText, signUpPinEditText)) {
                viewModel.signup(
                    signUpUsernameEditText.text.toString(),
                    signUpEmailEditText.text.toString(),
                    signUpPinEditText.text.toString(),
                    this
                )

                viewModel.isUserCreated.observe(this, Observer {
                    if (it) {
                        val intent = Intent(this, LogIn::class.java)
                        startActivity(intent)
                    } else
                        Toast.makeText(this, "Unexpected Error", Toast.LENGTH_SHORT).show()
                })
            }
            signUpProgressBar.visibility = View.INVISIBLE
        }

        loginTxt.setOnClickListener()
        {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

    }

    private fun canClick(
        username: EditText,
        email: EditText,
        pin: EditText
    ): Boolean {
        var value = false
        if (baseClass.isOnline(applicationContext)) {

            if (TextUtils.isEmpty(username.text)
                || TextUtils.isEmpty(email.text)
                || TextUtils.isEmpty(pin.text)
            ) {
                Toast.makeText(
                    baseContext,
                    "Mind if you fill the inputs?",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                if (pin.text.length < 6) {
                    Toast.makeText(
                        baseContext,
                        "Pin should be at least 6 characters",
                        Toast.LENGTH_SHORT
                    ).show()


                } else value = true
            }
        } else {
            Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT)
                .show()
        }
        return value
    }
}