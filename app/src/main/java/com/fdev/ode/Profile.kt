package com.fdev.ode

import android.app.ActivityOptions
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Profile : AppCompatActivity() {

    private val baseClass = BaseClass()
    private var blackFilter: ImageView? = null
    private var areYouSureCard: CardView? = null
    private var deleteButton: Button? = null
    private var dontDeleteButton: Button? = null
    private lateinit var logo: ImageView

    var scrollLayout: RelativeLayout? = null
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val username = findViewById<TextView>(R.id.username)
        val usermail = findViewById<TextView>(R.id.userMail)
        logo = findViewById(R.id.profileLogo)

        scrollLayout = findViewById(R.id.scroll_Relative)
        blackFilter = findViewById(R.id.profileBlackFilter)
        areYouSureCard = findViewById(R.id.deleteContactCard)
        deleteButton = findViewById(R.id.deleteContactBtn)
        dontDeleteButton = findViewById(R.id.notDeleteContactBtn)

        loadContacts()

        if (user != null) usermail.text = user.email

        //Set Username
        db.collection("Users").document(user?.email.toString()).get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    username.setText(document.getString("username"))
                }
            }

        //Copy mail adress
        logo.setOnClickListener() {
            Toast.makeText(this, "Cleveeer", Toast.LENGTH_SHORT).show()
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", user?.email.toString())
            clipboardManager.setPrimaryClip(clipData)
        }

    }

    private fun loadContacts() {
        val docRef = db.collection("Contacts").document(user?.email.toString())
        var myContact = ArrayList<String?>()
        var ContactNames = ArrayList<String?>()
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    myContact = document.get("contact") as ArrayList<String?>
                    ContactNames = document.get("contactName") as ArrayList<String?>

                    if (myContact.size != 0) { //User has contacts
                        for (i in myContact.indices) {

                            val sizeHeight = baseClass.getScreenHeight(this) * 0.5
                            val sizeWidth = baseClass.getScreenWidth(this)
                            val font = resources.getFont(R.font.plusjakartatextregular)
                            val boldFont = resources.getFont(R.font.plusjakartatexbold)

                            val contactNameText = TextView(this)
                            contactNameText.textSize = 20f
                            contactNameText.text = ContactNames[i].toString()
                            contactNameText.typeface = boldFont
                            scrollLayout?.addView(contactNameText)
                            baseClass.setMargins(
                                contactNameText,
                                (sizeWidth * 0.1).toInt(),
                                ((i * sizeHeight) * 0.2).toInt(),
                                25,
                                1
                            )

                            val contactMailText = TextView(this)
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

                            val deleteText = ImageButton(this)
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
                                blackFilter?.visibility = View.VISIBLE
                                areYouSureCard?.visibility = View.VISIBLE
                                baseClass.setViewsDisabled(listOf(logo))

                                dontDeleteButton?.setOnClickListener()
                                {
                                    blackFilter?.visibility = View.INVISIBLE
                                    areYouSureCard?.visibility = View.INVISIBLE
                                    baseClass.setViewsEnabled(listOf(logo))
                                }

                                deleteButton?.setOnClickListener() {
                                    deleteContact(myContact, ContactNames, i)
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
        ContactNames: ArrayList<String?>,
        i: Int
    ) {

        myContact.removeAt(i) //delete mail address
        ContactNames.removeAt(i) // delete name

        //Update the data with new ArrayList
        db.collection("Contacts").document(user?.email.toString())
            .update("contact", myContact)

        db.collection("Contacts").document(user?.email.toString())
            .update("contactName", ContactNames)

        finish()
        startActivity(getIntent(), ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

    }
}