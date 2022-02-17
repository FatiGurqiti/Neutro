package com.fdev.ode.util

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DebtController {

    private val db = Firebase.firestore

    fun AddDebt(amount: Long,to: String,update: String){
        var newDebt:Long //Works both for debt and to-collect depending on the update String
        val TAG = "AddNewDebt"
        val docRef = db.collection("Users").document(to)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Get Old data
                    Log.d(TAG, "DocumentSnapshot data: ${document.get(update)}")
                    var currectDebt = document?.getLong(update)
                    if (currectDebt != null) {
                        newDebt = currectDebt + amount
                    }
                    else newDebt = amount

                    //Upload new Data
                    db.collection("Users").document(to)
                        .update(update, newDebt)

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    fun GetDebtOrToCollect(type: String): String {
        val TAG = "GetDebtOrToCollect"
        val user = Firebase.auth.currentUser
        var value = "0"
        val docRef = db.collection("Users").document(user!!.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Get Old data
                    Log.d(TAG, "DocumentSnapshot data: ${document.get(type)}")
                    value = document.getLong(type).toString()
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                value = "0"
            }
        return value
    }



}