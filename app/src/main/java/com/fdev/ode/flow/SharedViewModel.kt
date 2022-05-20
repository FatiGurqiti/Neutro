package com.fdev.ode.flow

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class SharedViewModel : ViewModel() {

    private val db = Firebase.firestore

    val username: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun usernameQuerry(mail: String,lifecycleOwner: LifecycleOwner) {
        val docRef: DocumentReference = db.collection("Users").document(mail)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    username.value = document.get("username") as String
                }
            }
    }
}