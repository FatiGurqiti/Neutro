package com.fdev.ode.flow.forgotPassword

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fdev.ode.util.BaseClass
import com.fdev.ode.R
import com.fdev.ode.flow.login.LogIn
import com.fdev.ode.util.Toasts
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*


class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val baseClass = BaseClass()
        val toast = Toasts()

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
                                toast.emailSent(applicationContext)

                                Thread.sleep(2000L)
                                startActivity(Intent(applicationContext, LogIn::class.java))

                            } else toast.noSuchUser(applicationContext)

                        }
                }
            } else toast.checkInternet(applicationContext)

            progressBar.visibility = View.INVISIBLE
        }
    }
}