package com.fdev.ode.flow.profile

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fdev.ode.BaseClass
import com.fdev.ode.R
import com.fdev.ode.util.Toasts
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileViewModel : ViewModel() {

    private val toast = Toasts()
    private val baseClass = BaseClass()
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    val isDeleted: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun loadContacts(
        resources: Resources,
        profile: Activity,
        scrollLayout: RelativeLayout?,
        blackFilter: ImageView?,
        areYouSureCard: CardView?,
        logo: ImageView,
        dontDeleteButton: Button?,
        deleteButton: Button?,
        context: Context
    ) {
        val docRef = db.collection("Contacts").document(user?.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    val myContact = document?.get("contact") as ArrayList<String?>
                    val contactName = document?.get("contactName") as ArrayList<String?>

                    if (myContact.size != 0) { //User has contacts
                        for (i in myContact.indices) {

                            val sizeHeight = baseClass.getScreenHeight(profile) * 0.5
                            val sizeWidth = baseClass.getScreenWidth(profile)
                            val font = resources.getFont(R.font.plusjakartatextregular)
                            val boldFont = resources.getFont(R.font.plusjakartatexbold)

                            val contactNameText = TextView(profile.applicationContext)
                            contactNameText.textSize = 20f
                            contactNameText.text = contactName[i].toString()
                            contactNameText.typeface = boldFont
                            scrollLayout?.addView(contactNameText)
                            baseClass.setMargins(
                                contactNameText,
                                (sizeWidth * 0.1).toInt(),
                                ((i * sizeHeight) * 0.2).toInt(),
                                25,
                                1
                            )

                            val contactMailText = TextView(profile.applicationContext)
                            contactMailText.textSize = 20f
                            contactMailText.text = myContact[i].toString()
                            contactMailText.typeface = font
                            scrollLayout?.addView(contactMailText)
                            baseClass.setMargins(
                                contactMailText,
                                (sizeWidth * 0.1).toInt(),
                                (((i * sizeHeight) * 0.2) + ((sizeHeight) * 0.08)).toInt(),
                                25,
                                1
                            )

                            val deleteText = ImageButton(profile.applicationContext)
                            deleteText.setImageResource(R.drawable.delete);
                            deleteText.setBackgroundColor(Color.TRANSPARENT)
                            scrollLayout?.addView(deleteText)
                            baseClass.setMargins(
                                deleteText,
                                (sizeWidth * 0.8).toInt(),
                                (((i * sizeHeight) * 0.2)).toInt(),
                                0,
                                0
                            )

                            deleteText.setOnClickListener()
                            {
                                db.collection("Debts").document(user?.email.toString()).get()
                                    .addOnSuccessListener { document ->
                                        if (document.data != null) {
                                            if (myContact[i] in document.get("to") as ArrayList<*>)
                                                toast.unfinishedBusiness(context)
                                            else {
                                                db.collection("Recivements")
                                                    .document(user?.email.toString()).get()
                                                    .addOnSuccessListener {

                                                        if (it.data != null) {
                                                            if (myContact[i] in it.get("to") as ArrayList<*>)
                                                                toast.unfinishedBusiness(context)
                                                            else {
                                                                blackFilter?.visibility = View.VISIBLE
                                                                areYouSureCard?.visibility = View.VISIBLE
                                                                baseClass.setViewsDisabled(listOf(logo))
                                                            }
                                                        }
                                                    }
                                            }
                                        }
                                    }

                                dontDeleteButton?.setOnClickListener()
                                {
                                    blackFilter?.visibility = View.INVISIBLE
                                    areYouSureCard?.visibility = View.INVISIBLE
                                    baseClass.setViewsEnabled(listOf(logo))
                                }

                                deleteButton?.setOnClickListener() {
                                    deleteContact(myContact, contactName, i)
                                    baseClass.setViewsEnabled(listOf(logo))
                                }
                            }
                        }
                    }
                }
            }
    }


    private fun deleteContact(
        myContact: ArrayList<String?>,
        contactName: ArrayList<String?>,
        i: Int
    ) {

        runBlocking {
            launch {
                delay(1000L)
                isDeleted.value = true
            }

            myContact.removeAt(i) //delete mail address
            contactName.removeAt(i) // delete name

            //Update the data with new ArrayList
            db.collection("Contacts").document(user?.email.toString())
                .update("contact", myContact)

            db.collection("Contacts").document(user?.email.toString())
                .update("contactName", contactName)
        }

    }

    fun setUserName(username: TextView) {
        db.collection("Users").document(user?.email.toString()).get()
            .addOnSuccessListener { document ->
                if (document.data != null)
                    username.text = document?.getString("username")
            }
    }

}




