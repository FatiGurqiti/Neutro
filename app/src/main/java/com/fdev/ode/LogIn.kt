package com.fdev.ode

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LogIn : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var email: EditText
    private lateinit var pin: EditText
    private val baseClass = BaseClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val user = Firebase.auth.currentUser
        val signUpBtn = findViewById<TextView>(R.id.signUpTxt)
        val forgotPassword = findViewById<TextView>(R.id.forgotPassword)
        val login = findViewById<ImageButton>(R.id.logoinBtn)
        progressBar = findViewById(R.id.progressBarInLogIn)
        email = findViewById(R.id.loginEmail)
        pin = findViewById(R.id.loginPassword)

        if (user != null)
            email.setText(user.email.toString())

        progressBar.bringToFront()
        progressBar.visibility = View.INVISIBLE

        forgotPassword.setOnClickListener()
        {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        signUpBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            progressBar.visibility = View.INVISIBLE
        }

        login.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            if (baseClass.isOnline(applicationContext)) {
                val emailTxt = email.text.toString()
                val pinTxt = pin.text.toString()

                if (TextUtils.isEmpty(emailTxt)
                    || TextUtils.isEmpty(pinTxt)
                    || emailTxt.length < 6
                )
                    Toast.makeText(this, "Mind if you fill the inputs?", Toast.LENGTH_SHORT).show()
                else {

                    auth = Firebase.auth
                    auth.signInWithEmailAndPassword(emailTxt, pinTxt)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information

                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                        }
                }
            } else
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT)
                    .show()

            progressBar.visibility = View.INVISIBLE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        progressBar.visibility = View.INVISIBLE
    }
}