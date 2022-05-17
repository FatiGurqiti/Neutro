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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private var canclick = false
    private val baseClass = BaseClass()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val progressBar = findViewById<ProgressBar>(R.id.signUpProgressBar)
        auth = Firebase.auth
        val signUpBtn = findViewById<ImageButton>(R.id.signupBtn)

        signUpBtn.setOnClickListener {

            if (baseClass.isOnline(applicationContext)) {
                progressBar.visibility = View.VISIBLE

                val username = findViewById<EditText>(R.id.signUpUsernameEditText)
                val email = findViewById<EditText>(R.id.signUpEmailEditText)
                val pin = findViewById<EditText>(R.id.signUpPinEditText)

                val usernameTxt = username.text.toString()
                val emailTxt = email.text.toString()
                val pinTxt = pin.text.toString()

                if (TextUtils.isEmpty(usernameTxt)
                    || TextUtils.isEmpty(emailTxt)
                    || TextUtils.isEmpty(pinTxt)
                )
                    Toast.makeText(this, "Mind if you fill the inputs?", Toast.LENGTH_SHORT).show()

                else {
                    if (pinTxt.length < 6) {
                        Toast.makeText(
                            this,
                            "Pin should be at least 6 chacters",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {

                        val docRef = db.collection("Users").document(emailTxt)

                        docRef.get()
                            .addOnSuccessListener { document ->
                                if (document.data != null) { //user exists
                                    Toast.makeText(
                                        this,
                                        "This user is already exists",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else { //User doesn't exist

                                    CreateUser(emailTxt, pinTxt)  //To create auth
                                    UserData(
                                        usernameTxt,
                                        emailTxt,
                                        pinTxt
                                    ) //To save userdata to firestore

                                    Toast.makeText(
                                        this,
                                        "Account created successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(this, LogIn::class.java)
                                    startActivity(intent)
                                }
                            }
                    }
                }
            } else {
                Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT)
                    .show()
            }
            progressBar.visibility = View.INVISIBLE
        }


        val loginTxt = findViewById<TextView>(R.id.loginTxt)
        loginTxt.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }

    private fun CreateUser(Email: String, Pin: String) {
        auth.createUserWithEmailAndPassword(Email, Pin)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)  canclick = true
                else {
                    // If sign in fails, display a message to the user.
                    canclick = false
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun UserData(Username: String, Email: String, Pin: String) {
        val user = hashMapOf(
            "username" to Username,
            "email" to Email,
            "pin" to Pin
        )
        db.collection("Users").document(Email).set(user)

    }
}