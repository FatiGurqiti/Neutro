package com.fdev.ode.flow.forgotPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fdev.ode.BaseClass
import com.fdev.ode.R
import com.fdev.ode.flow.login.LogIn
import com.google.firebase.auth.FirebaseAuth


class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val baseClass = BaseClass()
        val resetButton = findViewById<Button>(R.id.reset)
        val resetPassword = findViewById<EditText>(R.id.emailReset)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val forgotPassword = findViewById<TextView>(R.id.forgotPasswordText)

        resetButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            if (baseClass.isOnline(applicationContext)) {
                if (!resetPassword.text.isNullOrEmpty()) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(resetPassword.text.toString())
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                resetButton.visibility = View.GONE
                                resetPassword.visibility = View.GONE
                                forgotPassword.text = "Please check your mailbox"

                                val toast = Toast.makeText(
                                    applicationContext,
                                    "An email is sent to your mailbox",
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()

                                Thread.sleep(2000L)
                                startActivity(Intent(applicationContext, LogIn::class.java))

                            } else {
                                val toast = Toast.makeText(
                                    applicationContext,
                                    "No such user",
                                    Toast.LENGTH_SHORT
                                )
                                toast.show()
                            }
                        }
                }
            } else
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT)
                    .show()

            progressBar.visibility = View.INVISIBLE
        }
    }
}