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

        val progressBar = findViewById<ProgressBar>(R.id.progressBarinSignUp)
        auth = Firebase.auth

        //SignUp
        val SignUpButton = findViewById<ImageButton>(R.id.SignBtn)
        SignUpButton.setOnClickListener {

            if (baseClass.isOnline(applicationContext)) {
                progressBar.visibility = View.VISIBLE

                val username = findViewById<EditText>(R.id.editTextTextUsernameinSignup)
                val email = findViewById<EditText>(R.id.editTextTextEmailAddressinSignup)
                val pin = findViewById<EditText>(R.id.editTextNumberPasswordinSignup)

                val Username = username.text.toString()
                val Email = email.text.toString()
                val Pin = pin.text.toString()

                if (TextUtils.isEmpty(Username)
                    || TextUtils.isEmpty(Email)
                    || TextUtils.isEmpty(Pin)
                ) {
                    //Inputs are empty
                    Toast.makeText(this, "Mind if you fill the inputs?", Toast.LENGTH_SHORT).show()
                } else {
                    //Inputs are filled
                    if (Pin.length < 6) {
                        Toast.makeText(
                            this,
                            "Pin should be at least 6 chacters",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    } else {

                        val docRef = db.collection("Users").document(Email)

                        docRef.get()
                            .addOnSuccessListener { document ->
                                if (document.data != null) { //user exists
                                    Toast.makeText(
                                        this,
                                        "This user is already exists",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                } else { //User doesn't exist

                                    CreateUser(Email, Pin)  //To create auth
                                    UserData(Username, Email, Pin) //To save userdata to firestore

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


        //Open Login Activity
        val LoginTxt = findViewById<TextView>(R.id.loginTxt)
        LoginTxt.setOnClickListener {
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }


    }


    private fun CreateUser(Email: String, Pin: String) {
        var TAG = "CreateUserAuth"
        auth.createUserWithEmailAndPassword(Email, Pin)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    canclick = true
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    canclick = false
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun UserData(Username: String, Email: String, Pin: String) {
        // Create a new user with a first and last name
        val user = hashMapOf(
            "username" to Username,
            "email" to Email,
            "pin" to Pin
        )

        db.collection("Users").document(Email).set(user)

    }
}