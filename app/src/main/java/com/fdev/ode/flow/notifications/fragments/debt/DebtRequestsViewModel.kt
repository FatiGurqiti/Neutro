package com.fdev.ode.flow.notifications.fragments.debt

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

                    updateRequest("id", idList, index)
                    updateRequest(
                        "receiverMail",
                        document.get("receiverMail") as ArrayList<String>,
                        index
                    )
                    updateRequest(
                        "receiverName",
                        document.get("receiverName") as ArrayList<String>,
                        index
                    )
                    updateRequest(
                        "senderMail",
                        document.get("senderMail") as ArrayList<String>,
                        index
                    )
                    updateRequest(
                        "senderName",
                        document.get("senderName") as ArrayList<String>,
                        index
                    )
                    updateRequest("label", document.get("label") as ArrayList<String>, index)
                    updateRequest("time", document.get("time") as ArrayList<String>, index)

                    val amountList = document.get("amount") as ArrayList<String>
                    amountList.removeAt(index)

                    db.collection("DebtRequests").document(user?.email.toString())
                        .update("amount", amountList)
                }
            }
    }


    fun approveDebt(
        id: String,
        mail: String,
        name: String,
        label: String,
        time: String,
        amount: Double,
        collections: String,
        documentPath: String
    ) {
        val idArray = arrayListOf(id)
        val mailArray = arrayListOf(mail)
        val nameArray = arrayListOf(name)
        val labelArray = arrayListOf(label)
        val timeArray = arrayListOf(time)
        val amountArray = arrayListOf(amount)

        val docRef = db.collection(collections).document(documentPath)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    updateDebt(idArray, "id", document, collections, documentPath)
                    updateDebt(mailArray, "to", document, collections, documentPath)
                    updateDebt(nameArray, "name", document, collections, documentPath)
                    updateDebt(labelArray, "label", document, collections, documentPath)
                    updateDebt(timeArray, "time", document, collections, documentPath)

                    amountArray.plusAssign(document.get("amount") as ArrayList<Double>)
                    db.collection(collections).document(documentPath)
                        .update("amount", amountArray)

                } else {
                    val debtHash = hashMapOf(
                        "id" to idArray,
                        "to" to mailArray,
                        "name" to nameArray,
                        "amount" to amountArray,
                        "label" to labelArray,
                        "time" to timeArray
                    )

                    db.collection(collections)
                        .document(documentPath)
                        .set(debtHash)
                }
            }
    }

    private fun updateRequest(value: String, list: ArrayList<String>, index: Int) {
        list.removeAt(index)
        db.collection("DebtRequests").document(user?.email.toString())
            .update(value, list)
    }

    private fun updateDebt(
        list: ArrayList<String>,
        value: String,
        document: DocumentSnapshot,
        collections: String,
        documentPath: String
    ) {
        list.plusAssign(document.get(value) as ArrayList<String>)
        db.collection(collections).document(documentPath)
            .update(value, list)
    }

    private fun toStringArray(doubleList: ArrayList<Double>): ArrayList<String> {
        val stringList = ArrayList<String>()
        for (i in doubleList.indices)
            stringList.add(doubleList[i].toString())

        return stringList
    }
}