package com.fdev.ode

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LogIn : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progressBar = findViewById(R.id.progressBarInLogIn)
        val SignUpbtn = findViewById<TextView>(R.id.SignUpTxt)
        val forgotpassword = findViewById<TextView>(R.id.forgotPassword)
        val LogIn = findViewById<ImageButton>(R.id.logoinBtn)

        progressBar.bringToFront()
        progressBar.visibility = View.INVISIBLE

        forgotpassword.setOnClickListener()
        {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

        SignUpbtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            progressBar.visibility = View.INVISIBLE
        }

        LogIn.setOnClickListener {
            progressBar.visibility = View.VISIBLE

            val email = findViewById<EditText>(R.id.Email)
            val pin = findViewById<EditText>(R.id.password)

            val Email = email.text.toString()
            val Pin = pin.text.toString()

            if (TextUtils.isEmpty(Email)
                || TextUtils.isEmpty(Pin)
                || Email.length < 6
            ) {
                //Inputs are empty
                Toast.makeText(this, "Mind if you fill the inputs?", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.INVISIBLE
            } else {
                var TAG = "Login"

                auth = Firebase.auth
                auth.signInWithEmailAndPassword(Email, Pin)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")

                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }


            }

          
        }
    }
}