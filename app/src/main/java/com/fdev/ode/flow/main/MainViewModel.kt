package com.fdev.ode.flow.main

import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    fun getContactUserName(
        generatedID: String,
        contactMailTxt: String,
        label: String,
        amount: String
    ) {
        db.collection("Users").document(user?.email.toString()).get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    addDebtOrReceivement(
                        generatedID,
                        contactMailTxt,
                        document.getString("username").toString(),
                        label,
                        amount.toDouble(),
                        "Debts"
                    )
                }
            }
    }

     fun addFreshData(myContact: ArrayList<String?>, email: String, to: String) {
        val contactName = ArrayList<String?>()
        val docRef = db.collection("Users").document(email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Update username
                    contactName.add(document.getString("username"))

                    val contactHash = hashMapOf(
                        "contact" to myContact,
                        "contactName" to contactName
                    )
                    db.collection("Contacts").document(to)
                        .set(contactHash)
                }
            }
    }


    fun getDebtOrRecievement(type: String, totalText: TextView?, totalAmount: TextView?) {
        if (type == "Debts") totalText?.text = "Total Debt"
        else totalText?.setText("Total Receivement")

        val user = Firebase.auth.currentUser
        val docRef = db.collection(type).document(user!!.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Get Old data
                    val valueArray: ArrayList<Double?> =
                        document.get("amount") as ArrayList<Double?>
                    var value = 0.0
                    // Add new data on top of old data
                    for (i in valueArray.indices)
                        value += valueArray[i]!!.toDouble()
                    totalAmount?.text = value.toString()
                }
            }
    }

    fun addDebtOrReceivement(
        id: String,
        to: String,
        name: String,
        label: String,
        amount: Double,
        type: String
    ) {
        var idArray = ArrayList<String?>()
        var toArray = ArrayList<String?>()
        var nameArray = ArrayList<String?>()
        var labelArray = ArrayList<String?>()
        var timeArray = ArrayList<String?>()
        var amountArray = ArrayList<Double?>()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = current.format(formatter)

        val toWhom: String
        val user: String
        val time = formatted.toString()

        if (type == "Recivements") {
            user = Firebase.auth.currentUser?.email.toString()
            toWhom = to
        } else {
            toWhom = Firebase.auth.currentUser?.email.toString()
            user = to
        }

        val docRef = db.collection(type).document(user)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //get Old data
                    idArray = document.get("id") as ArrayList<String?>
                    toArray = document.get("to") as ArrayList<String?>
                    nameArray = document.get("name") as ArrayList<String?>
                    labelArray = document.get("label") as ArrayList<String?>
                    timeArray = document.get("time") as ArrayList<String?>
                    amountArray = document.get("amount") as ArrayList<Double?>

                    //add data to it
                    idArray.add(id)
                    toArray.add(toWhom)
                    nameArray.add(name)
                    labelArray.add(label)
                    timeArray.add(time)
                    amountArray.add(amount)

                    //update the data
                    db.collection(type)
                        .document(user)
                        .update("id", idArray)

                    db.collection(type)
                        .document(user)
                        .update("to", toArray)

                    db.collection(type)
                        .document(user)
                        .update("name", nameArray)

                    db.collection(type)
                        .document(user)
                        .update("label", labelArray)

                    db.collection(type)
                        .document(user)
                        .update("time", timeArray)

                    db.collection(type)
                        .document(user)
                        .update("amount", amountArray)

                } else {

                    idArray.add(id)
                    toArray.add(toWhom)
                    nameArray.add(name)
                    labelArray.add(label)
                    timeArray.add(time)
                    amountArray.add(amount)

                    val debthash = hashMapOf(
                        "id" to idArray,
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
    }


    fun generateId(): String {
        val charArr =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&'()*+,-./:;<=>?@[]^_`{|}~"
        var id = ""
        for (i in 0..64) {
            val random = Random.nextInt(0, 92)
            id += charArr.elementAt(random)
        }
        return id
    }

    fun preventDuplicatedData(list: ArrayList<String?>): ArrayList<String?> {
        for (i in 0..list.size) {
            for (j in i + 1..list.size) {
                if (j < list.size) {
                    if (list[i] == list[j])
                        list.removeAt(j)
                }
            }
        }
        return list
    }

}