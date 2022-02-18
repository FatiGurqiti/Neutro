package com.fdev.ode.util

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DebtController {

    private val db = Firebase.firestore

    //Adds Total debt
    fun AddTotalDebt(amount: Long,to: String,update: String) {
        var newDebt: Long //Works both for debt and to-collect depending on the update String
        val TAG = "AddTotalDebt"
        val docRef = db.collection("Users").document(to)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Get Old data
                    Log.d(TAG, "DocumentSnapshot data: ${document.get(update)}")
                    var currectDebt = document?.getLong(update)
                    if (currectDebt != null) {
                        newDebt = currectDebt + amount
                    } else newDebt = amount

                    //Upload new Data
                    db.collection("Users").document(to)
                        .update(update, newDebt)

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

     //Substract Total debt
    fun SubstractTotalDebt(amount: Long,to: String,update: String){
        var newDebt:Long //Works both for debt and to-collect depending on the update String
        val TAG = "AddTotalDebt"
        val docRef = db.collection("Users").document(to)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Get Old data
                    Log.d(TAG, "DocumentSnapshot data: ${document.get(update)}")
                    var currectDebt = document?.getLong(update)
                    if (currectDebt != null) {
                        newDebt = currectDebt - amount
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

    fun AddDebtAndReceivement(to: String,name: String, label: String,amount: Long,type: String) {
        //variable "type" stands for either debt or Receivement
        //This allows to do two works in one function

        var toArray = ArrayList<String?>()
        var nameArray = ArrayList<String?>()
        var labelArray = ArrayList<String?>()
        var amountArray = ArrayList<Long?>()

        val ToWhom:String
        val user: String
        if(type == "Recivements")
        {
             user = Firebase.auth.currentUser?.email.toString()
             ToWhom = to
        }
        else
        {
            ToWhom = Firebase.auth.currentUser?.email.toString()
            user = to
        }


        val TAG = "AddDebt"
        val docRef = db.collection(type).document(user)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //get Old data
                    toArray = document.get("to") as ArrayList<String?>
                    nameArray = document.get("name") as ArrayList<String?>
                    labelArray  = document.get("label") as ArrayList<String?>
                    amountArray = document.get("amount") as ArrayList<Long?>

                    //add data to it
                    toArray.add(ToWhom)
                    nameArray.add(name)
                    labelArray.add(label)
                    amountArray.add(amount)

                    //update the data
                    db.collection(type)
                        .document(user)
                        .update("to",toArray)

                    db.collection(type)
                        .document(user)
                        .update("name",nameArray)

                    db.collection(type)
                        .document(user)
                        .update("label",labelArray)

                    db.collection(type)
                        .document(user)
                        .update("amount",amountArray)

                }
                else
                {
                    Log.d(TAG, "No Such document")

                    toArray.add(ToWhom)
                    nameArray.add(name)
                    labelArray.add(label)
                    amountArray.add(amount)

                    var debthash = hashMapOf(
                        "to" to toArray,
                        "name" to nameArray,
                        "amount" to amountArray,
                        "label" to labelArray
                    )

                    db.collection(type)
                        .document(user)
                        .set(debthash)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }



}