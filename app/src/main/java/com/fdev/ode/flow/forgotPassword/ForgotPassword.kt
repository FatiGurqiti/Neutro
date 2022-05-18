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
import kotlinx.android.synthetic.main.activity_forgot_password.*


class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val baseClass = BaseClass()

        reset.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            if (baseClass.isOnline(applicationContext)) {
                if (!emailReset.text.isNullOrEmpty()) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailReset.text.toString())
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                reset.visibility = View.GONE
                                emailReset.visibility = View.GONE
                                forgotPasswordText.text = "Please check your mailbox"

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