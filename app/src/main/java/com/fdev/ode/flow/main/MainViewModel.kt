package com.fdev.ode.flow.main

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fdev.ode.BaseClass
import com.fdev.ode.R
import com.fdev.ode.util.Toasts
import com.google.firebase.auth.ktx.auth
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

    val username: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun ifHasNotification(notificationsBtn: ImageButton, collection: String) {
        val contactDocRef = db.collection(collection).document(user?.email.toString())
        contactDocRef.get()
            .addOnSuccessListener { document ->
                if (document.data == null)
                    notificationsBtn.setImageResource(R.drawable.bell)
                else
                    notificationsBtn.setImageResource(R.drawable.bell_notification)
            }

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

    fun getDebtOrRecievement(type: String, totalText: TextView?, totalAmount: TextView?) {

        if (type == "Debts") totalText?.text = "Total Debt"
        else totalText?.text = "Total Receivement"

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

    fun sendDebtRequest(id: String, mail: String, name: String,senderName:String, label: String, amount: Double) {

        val idArray = ArrayList<String?>()
        val receiverMailArray = ArrayList<String?>()
        val receiverNameArray = ArrayList<String?>()
        val senderMailArray = ArrayList<String?>()
        val senderNameArray = ArrayList<String?>()
        val labelArray = ArrayList<String?>()
        val timeArray = ArrayList<String?>()
        val amountArray = ArrayList<Double?>()

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        idArray.add(id)
        receiverMailArray.add(mail)
        receiverNameArray.add(name)
        senderMailArray.add(user?.email.toString())
        senderNameArray.add(senderName)
        labelArray.add(label)
        timeArray.add(current.format(formatter).toString())
        amountArray.add(amount)

        val docRef = db.collection("DebtRequests").document(mail)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    idArray.plusAssign(document?.get("id") as ArrayList<String?>)
                    receiverMailArray.plusAssign(document.get("receiverMail") as ArrayList<String?>)
                    receiverNameArray.plusAssign(document.get("receiverName") as ArrayList<String?>)
                    senderMailArray.plusAssign(document.get("senderMail") as ArrayList<String?>)
                    senderNameArray.plusAssign(document.get("senderName") as ArrayList<String?>)
                    labelArray.plusAssign(document.get("label") as ArrayList<String?>)
                    timeArray.plusAssign(document.get("time") as ArrayList<String?>)
                    amountArray.plusAssign(document.get("amount") as ArrayList<Double?>)

                    updateDoc(mail, "id", idArray)
                    updateDoc(mail, "receiverMail", receiverMailArray)
                    updateDoc(mail, "receiverName", receiverNameArray)
                    updateDoc(mail, "senderMail", senderMailArray)
                    updateDoc(mail, "senderName", senderNameArray)
                    updateDoc(mail, "label", labelArray)
                    updateDoc(mail, "time", timeArray)

                    db.collection("DebtRequests")
                        .document(mail)
                        .update("amount", amountArray)

                } else {

                    val debthash = hashMapOf(
                        "id" to idArray,
                        "receiverMail" to receiverMailArray,
                        "receiverName" to receiverNameArray,
                        "senderMail" to senderMailArray,
                        "senderName" to senderNameArray,
                        "amount" to amountArray,
                        "label" to labelArray,
                        "time" to timeArray
                    )

                    db.collection("DebtRequests")
                        .document(mail)
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
        docRef.get().addOnSuccessListener { document ->
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

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = current.format(formatter).toString()
        var mails = ArrayList<String?>()
        var times = ArrayList<String?>()

        mails.add(usersMail)
        times.add(formatted)

        val docRef = db.collection("ContactRequests").document(contactMail)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    mails += document.get("mail") as ArrayList<String?>
                    mails = preventDuplicatedData(mails)
                    db.collection("ContactRequests").document(contactMail)
                        .update("mail", mails)


                    times = preventDuplicatedData(times)
                    times += document.get("date") as ArrayList<String?>
                    db.collection("ContactRequests").document(contactMail)
                        .update("date", times)

                } else {
                    val contactHash = hashMapOf(
                        "mail" to mails,
                        "date" to times
                    )
                    db.collection("ContactRequests").document(contactMail)
                        .set(contactHash)
                }
            }
    }

    fun getUsername() {
        db.collection("Users").document(user?.email.toString()).get()
            .addOnSuccessListener { document ->
                if (document.data != null){
                    Log.d("whatisusername",document?.getString("username").toString())
                    username.value = document.getString("username")

                }
            }
    }

    private fun updateDoc(mail: String, field: String, value: ArrayList<String?>) {
        db.collection("DebtRequests")
            .document(mail)
            .update(field, value)
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