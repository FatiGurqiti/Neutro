package com.fdev.ode.flow.main

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fdev.ode.BaseClass
import com.fdev.ode.R
import com.fdev.ode.util.Toasts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val toast = Toasts()
    private val baseClass = BaseClass()
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    val refresh: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    val closeContactCard: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun loadContacts(
        scrollLayout: RelativeLayout,
        activity: MainActivity,
        resources: Resources
    ) {

        val docRef = db.collection("Contacts").document(user?.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    val myContact = document?.get("contact") as ArrayList<String?>
                    val contactNames = document?.get("contactName") as ArrayList<String?>

                    if (myContact.size != 0) { //User has contacts

                        for (i in myContact.indices) {
                            if (myContact[i] != null && contactNames[i] != null) {

                                val sizeHeight = baseClass.getScreenHeight(activity) * 0.5
                                val sizeWidth = baseClass.getScreenWidth(activity)
                                val font = resources.getFont(R.font.plusjakartatextregular)
                                val boldFont = resources.getFont(R.font.plusjakartatexbold)

                                val contactNameTxt = TextView(activity)
                                contactNameTxt.textSize = 20f
                                contactNameTxt.text = contactNames[i].toString()
                                contactNameTxt.layoutParams = RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                                )
                                contactNameTxt.typeface = boldFont
                                contactNameTxt.translationZ = 20F
                                scrollLayout.addView(contactNameTxt)
                                baseClass.setMargins(
                                    contactNameTxt,
                                    (sizeWidth * 0.1).toInt(),
                                    ((i * sizeHeight) * 0.2).toInt(),
                                    0,
                                    0
                                )

                                contactNameTxt.setOnClickListener()
                                {
                                    activity.setContactNameAndMail(
                                        contactNames[i].toString(),
                                        myContact[i].toString()
                                    )

                                }

                                val contactMailTxt = TextView(activity)
                                contactMailTxt.textSize = 20f
                                contactMailTxt.text = myContact[i].toString()
                                contactMailTxt.typeface = font
                                contactMailTxt.translationZ = 20F
                                contactMailTxt.layoutParams = RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                                )
                                scrollLayout.addView(contactMailTxt)
                                baseClass.setMargins(
                                    contactMailTxt,
                                    (sizeWidth * 0.1).toInt(),
                                    (((i * sizeHeight) * 0.2) + ((sizeHeight) * 0.08)).toInt(),
                                    0,
                                    0
                                )

                                contactMailTxt.setOnClickListener {
                                    activity.setContactNameAndMail(
                                        contactNames[i]!!,
                                        myContact[i]!!
                                    )
                                }
                            }
                        }
                    }
                }
            }
    }

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
                        document?.getString("username").toString(),
                        label,
                        amount.toDouble(),
                        "Debts"
                    )
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
                        document?.get("amount") as ArrayList<Double?>
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


    fun ifContactExist(email: String, context: Context) {

        val userMail = user!!.email.toString()
        val docRef = db.collection("Contacts").document(userMail)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    if (email in document.get("contact") as ArrayList<String>) {
                        toast.contactExists(context)
                        closeContactCard.value = false
                    } else
                        contactCheck(email, context)
                }
            }
    }

    private fun contactCheck(email: String, context: Context) {
        val userMail = user!!.email.toString()
        val docRef = db.collection("Users").document(email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    sendContactRequest(email, userMail)
                    toast.contactRequestSent(context)
                    closeContactCard.value = true

                } else {
                    toast.noSuchUser(context)
                    closeContactCard.value = false
                }
            }
    }

    private fun sendContactRequest(contactMail: String, usersMail: String) {

        var mails = ArrayList<String?>()
        mails.add(usersMail)
        val docRef = db.collection("ContactRequests").document(contactMail)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    mails += document.get("mail") as ArrayList<String>

                    mails = preventDuplicatedData(mails)
                    db.collection("ContactRequests").document(contactMail)
                        .update("mail", mails)

                } else {
                    val contactHash = hashMapOf(
                        "mail" to mails
                    )
                    db.collection("ContactRequests").document(contactMail)
                        .set(contactHash)
                }
            }
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