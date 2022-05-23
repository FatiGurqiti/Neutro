package com.fdev.ode.flow.notifications.fragments.contact

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ContactRequestsViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    val username = ArrayList<String>()
    val contactMail: MutableLiveData<ArrayList<String>> by lazy {
        MutableLiveData<ArrayList<String>>()
    }
    val contactUsername: MutableLiveData<ArrayList<String>> by lazy {
        MutableLiveData<ArrayList<String>>()
    }
    val contactDate: MutableLiveData<ArrayList<String>> by lazy {
        MutableLiveData<ArrayList<String>>()
    }

    init {
        val docRef: DocumentReference =
            db.collection("ContactRequests").document(user?.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    contactMail.value = document.get("mail") as ArrayList<String>
                    contactDate.value = document.get("date") as ArrayList<String>
                }
            }

        viewModelScope.launch {
            contactMail.asFlow().collect {
                for (i in it.indices) {
                    val docRef: DocumentReference = db.collection("Users").document(it[i])
                    docRef.get()
                        .addOnSuccessListener { document ->
                            if (document.data != null) {
                                username.add((document.get("username") as String))
                                contactUsername.value = username
                            }
                        }
                }
            }
        }
    }


    fun denyContact(email: String) {
        val docRef: DocumentReference =
            db.collection("ContactRequests").document(user?.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    var index = 0
                    val mailList = document?.get("mail") as ArrayList<String?>
                    val dateList = document.get("date") as ArrayList<String?>

                    for (i in mailList.indices) {
                        if (mailList[i].equals(email))
                            index = i
                    }

                    mailList.removeAt(index)
                    db.collection("ContactRequests").document(user?.email.toString())
                        .update("mail", mailList)

                    dateList.removeAt(index)
                    db.collection("ContactRequests").document(user?.email.toString())
                        .update("date", dateList)

                }
            }
    }

    fun approveContact(email: String, to: String) {
        var myContact = ArrayList<String?>() //email address
        val docRef: DocumentReference = db.collection("Contacts").document(to)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    //Get previous Contacts
                    myContact = document?.get("contact") as ArrayList<String?>
                    val contactName = document?.get("contactName") as ArrayList<String?>

                    //Get name of the current contanct
                    db.collection("Users")
                        .document(to)
                        .get()
                        .addOnSuccessListener {
                            if (it.data != null) {
                                val username = it.get("username")
                                contactName.add(username.toString())

                                myContact.add(email)
                                updateContact(myContact, email, to)
                            }
                        }
                } else {
                    // There is no previous data. So, simply add the current one
                    myContact.add(email)
                    addFreshData(myContact, email, to)
                }
            }
    }


    private fun updateContact(myContact: ArrayList<String?>, email: String, to: String) {

        val contact = preventDuplicatedData(myContact)
        db.collection("Contacts").document(to)
            .update("contact", contact)

        val docRef = db.collection("Contacts").document(to)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Get the old data
                    var contactNames = document?.get("contactName") as ArrayList<String?>

                    db.collection("Users")
                        .document(email)
                        .get()
                        .addOnSuccessListener {
                            if (it.data != null) {
                                contactNames.add(it.getString("username"))
                                contactNames = preventDuplicatedData(contactNames)

                                db.collection("Contacts").document(to)
                                    .update("contactName", contactNames)
                            }
                        }
                }
            }
    }


    private fun addFreshData(myContact: ArrayList<String?>, email: String, to: String) {
        val contactName = ArrayList<String?>()
        val docRef = db.collection("Users").document(email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Update username
                    contactName.add(document?.getString("username"))

                    val contactHash = hashMapOf(
                        "contact" to myContact,
                        "contactName" to contactName
                    )
                    db.collection("Contacts").document(to)
                        .set(contactHash)
                }
            }
    }


    private fun preventDuplicatedData(list: ArrayList<String?>): ArrayList<String?> {
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