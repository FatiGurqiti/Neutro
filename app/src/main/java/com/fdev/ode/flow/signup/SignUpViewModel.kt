package com.fdev.ode.flow.signup

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SignUpViewModel : ViewModel() {

    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    val isUserCreated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun signup(username: String, email: String, pin: String, activity: Activity) {

        auth = Firebase.auth
        val docRef = db.collection("Users").document(email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) { //user exists
                    Toast.makeText(
                        activity.applicationContext,
                        "This user is already exists",
                        Toast.LENGTH_SHORT
                    ).show()

                } else { //User doesn't exist
                    userData(username, email, pin)
                    auth.createUserWithEmailAndPassword(email, pin)

                    Toast.makeText(
                        activity.applicationContext,
                        "Account created successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    isUserCreated.value = true
                }
            }
    }

    private fun userData(userName: String, email: String, pin: String) {
        val user = hashMapOf(
            "username" to userName,
            "email" to email,
            "pin" to pin
        )
        db.collection("Users").document(email).set(user)
    }
}