package com.fdev.ode.flow.notifications.fragments.debt

import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DebtRequestsViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser
    private val detailsList = ArrayList<ArrayList<String>>()
    val debts: MutableLiveData<List<ArrayList<String>>> by lazy {
        MutableLiveData<List<ArrayList<String>>>()
    }

    init {
        db.collection("DebtRequests").document(user?.email.toString()).get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    detailsList.add(toStringArray(document.get("amount") as ArrayList<Double>))
                    detailsList.add(document.get("id") as ArrayList<String>)
                    detailsList.add(document.get("receiverMail") as ArrayList<String>)
                    detailsList.add(document.get("receiverName") as ArrayList<String>)
                    detailsList.add(document.get("senderMail") as ArrayList<String>)
                    detailsList.add(document.get("senderName") as ArrayList<String>)
                    detailsList.add(document.get("label") as ArrayList<String>)
                    detailsList.add(document.get("time") as ArrayList<String>)
                    debts.value = detailsList
                }
            }
    }

    fun denyContact(id: String) {
        val docRef: DocumentReference =
            db.collection("DebtRequests").document(user?.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    var index = 0
                    val idList = document.get("id") as ArrayList<String>

                    for (i in idList.indices) {
                        if (idList[i].equals(id))
                            index = i
                    }

                    update("id", idList, index)
                    update("receiverMail", document.get("receiverMail") as ArrayList<String>, index)
                    update("receiverName", document.get("receiverName") as ArrayList<String>, index)
                    update("senderMail", document.get("senderMail") as ArrayList<String>, index)
                    update("senderName", document.get("senderName") as ArrayList<String>, index)
                    update("label", document.get("label") as ArrayList<String>, index)
                    update("time", document.get("time") as ArrayList<String>, index)

                    val amountList = document.get("amount") as ArrayList<String>
                    amountList.removeAt(index)

                    db.collection("DebtRequests").document(user?.email.toString())
                        .update("amount", amountList)
                }
            }
    }

    private fun update(value: String, list: ArrayList<String>, index: Int) {
        list.removeAt(index)
        db.collection("DebtRequests").document(user?.email.toString())
            .update(value, list)
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
                    idArray = document?.get("id") as ArrayList<String?>
                    toArray = document?.get("to") as ArrayList<String?>
                    nameArray = document?.get("name") as ArrayList<String?>
                    labelArray = document?.get("label") as ArrayList<String?>
                    timeArray = document?.get("time") as ArrayList<String?>
                    amountArray = document?.get("amount") as ArrayList<Double?>

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

    private fun toStringArray(doubleList: ArrayList<Double>): ArrayList<String> {
        val stringList = ArrayList<String>()
        for (i in doubleList.indices)
            stringList.add(doubleList[i].toString())

        return stringList
    }
}