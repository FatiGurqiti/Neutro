package com.fdev.ode.util

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class DebtController {

    private val db = Firebase.firestore


    fun AddDebtAndReceivement(id: String, to: String,name: String, label: String,amount: Double,type: String) {
        //variable "type" stands for either debt or Receivement
        //This allows to do two works in one function

        var IDArray = ArrayList<String?>()
        var toArray = ArrayList<String?>()
        var nameArray = ArrayList<String?>()
        var labelArray = ArrayList<String?>()
        var timeArray = ArrayList<String?>()
        var amountArray = ArrayList<Double?>()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = current.format(formatter)

        val ToWhom:String
        val user: String
        val time = formatted.toString()
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
                    IDArray = document.get("id") as ArrayList<String?>
                    toArray = document.get("to") as ArrayList<String?>
                    nameArray = document.get("name") as ArrayList<String?>
                    labelArray  = document.get("label") as ArrayList<String?>
                    timeArray = document.get("time") as ArrayList<String?>
                    amountArray = document.get("amount") as ArrayList<Double?>

                    //add data to it
                    IDArray.add(id)
                    toArray.add(ToWhom)
                    nameArray.add(name)
                    labelArray.add(label)
                    timeArray.add(time)
                    amountArray.add(amount)

                    //update the data

                    db.collection(type)
                        .document(user)
                        .update("id",IDArray)

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
                        .update("time",timeArray)

                    db.collection(type)
                        .document(user)
                        .update("amount",amountArray)

                }
                else
                {
                    Log.d(TAG, "No Such document")

                    IDArray.add(id)
                    toArray.add(ToWhom)
                    nameArray.add(name)
                    labelArray.add(label)
                    timeArray.add(time)
                    amountArray.add(amount)

                    var debthash = hashMapOf(
                        "id" to IDArray,
                        "to" to toArray,
                        "name" to nameArray,
                        "amount" to amountArray,
                        "label" to labelArray,
                        "time" to timeArray
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


    fun GenerateID():String
    {
        val CharArr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*+,-./:;<=>?@[]^_`{|}~"
        var ID=""
        for(i in 0..64)
        {
            val Random = Random.nextInt(0,92)
            ID += CharArr.elementAt(Random)
        }
        return ID
    }

}