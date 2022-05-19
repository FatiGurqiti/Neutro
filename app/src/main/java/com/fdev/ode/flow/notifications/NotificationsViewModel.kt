package com.fdev.ode.flow.notifications

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotificationsViewModel : ViewModel() {

    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    val refresh: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    private fun addContact(email: String, to: String) {
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

                                refresh.value = true
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