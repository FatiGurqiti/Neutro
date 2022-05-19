package com.fdev.ode.flow.login

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fdev.ode.util.Toasts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
                } else toast.authFail(activity.applicationContext)
            }
    }
}