package com.fdev.ode.flow.login

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fdev.ode.util.Toasts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging

class LoginViewModel : ViewModel() {

    val toast = Toasts()
    val canLogin: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun signIn(email: String, pin: String, activity: Activity) {
        val auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, pin)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    canLogin.value = true
                    checkToken(email)
                } else toast.authFail(activity.applicationContext)
            }
    }

    private fun checkToken(mail: String) {
        val db = Firebase.firestore
        db.collection("Users").document(mail)
            .get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    FirebaseMessaging.getInstance().token.addOnSuccessListener {

                        if (it.toString() != document.getString("token").toString()) {
                            db.collection("Users").document(mail)
                                .update("token", it.toString())
                        }
                    }
                }
            }
    }
}