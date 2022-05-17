package com.fdev.ode

import android.app.ActivityOptions
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Profile : AppCompatActivity() {


    private var BlackFilter: ImageView? = null
    private var AreYouSureCard: CardView? = null
    private var Deletebutton: Button? = null
    private var DontDeletebutton: Button? = null

    var scrollLayout: RelativeLayout? = null
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val username = findViewById<TextView>(R.id.username)
        val usermail = findViewById<TextView>(R.id.userMail)
        val logo = findViewById<ImageView>(R.id.logoinProfile)
        scrollLayout = findViewById(R.id.Scroll_Relative)
        BlackFilter = findViewById(R.id.blackfilterinProfile)
        AreYouSureCard = findViewById(R.id.deleteContactCard)
        Deletebutton = findViewById(R.id.deleteContactBtn)
        DontDeletebutton = findViewById(R.id.notDeleteContactBtn)


        LoadContacts()

        if (user != null) {
            usermail.text = user.email
        }

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

    private fun LoadContacts() {
        //Load Contacts
        val TAG = "ViewContact"
        val docRef = db.collection("Contacts").document(user?.email.toString())
        var myContact = ArrayList<String?>()
        var ContactNames = ArrayList<String?>()
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    Log.d(
                        TAG,
                        "DocumentSnapshot data: ${document.get("contact") as ArrayList<String?>}"
                    )
                    myContact = document.get("contact") as ArrayList<String?>
                    ContactNames = document.get("contactName") as ArrayList<String?>
                    Log.d(TAG, "myContact: ${myContact}")

                    Log.d(TAG, myContact.size.toString())

                    if (myContact.size != 0) { //User has contacts

                        for (i in myContact.indices) {

                            val sizeheight = getScreenHeight(this) * 0.5
                            val sizewidth = getScreenWidth(this)

                            val face = resources.getFont(R.font.plusjakartatextregular)
                            val boldface = resources.getFont(R.font.plusjakartatexbold)

                            val Contact_Name = TextView(this)
                            Contact_Name.textSize = 20f
                            Contact_Name.text = nullprotect(ContactNames.get(i).toString())
                            Contact_Name.setTypeface(boldface)
                            scrollLayout?.addView(Contact_Name)
                            setMargins(
                                Contact_Name,
                                (sizewidth * 0.1).toInt(),
                                ((i * sizeheight) * 0.2).toInt(),
                                25,
                                1
                            )

                            val Contact_Mail = TextView(this)
                            Contact_Mail.textSize = 20f
                            Contact_Mail.text = nullprotect(myContact.get(i).toString())
                            Contact_Mail.setTypeface(face)
                            scrollLayout?.addView(Contact_Mail)
                            setMargins(
                                Contact_Mail,
                                (sizewidth * 0.1).toInt(),
                                (((i * sizeheight) * 0.2) + ((sizeheight) * 0.08)).toInt(),
                                25,
                                1
                            )

                            val Delete = ImageButton(this)
                            Delete.setImageResource(R.drawable.delete);
                            Delete.setBackgroundColor(Color.TRANSPARENT)
                            scrollLayout?.addView(Delete)
                            setMargins(
                                Delete,
                                (sizewidth * 0.8).toInt(),
                                (((i * sizeheight) * 0.2)).toInt(),
                                0,
                                0
                            )


                            // Confromation Buttons(Yes,No) must be inside of Delete(CardView) in order to get the correct index
                            Delete.setOnClickListener()
                            {
                                BlackFilter?.visibility = View.VISIBLE
                                AreYouSureCard?.visibility = View.VISIBLE

                                //Don't Delete
                                DontDeletebutton?.setOnClickListener()
                                {
                                    BlackFilter?.visibility = View.INVISIBLE
                                    AreYouSureCard?.visibility = View.INVISIBLE
                                }
                                //Delete Contact
                                Deletebutton?.setOnClickListener() {
                                    deleteContact(myContact, ContactNames, i)
                                }
                            }


                        }
                    }


                } else {
                    Log.d(TAG, "No such document")

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
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


    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.getLayoutParams() is MarginLayoutParams) {
            val p = v.getLayoutParams() as MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }
    }

    fun getScreenHeight(context: Context?): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun getScreenWidth(context: Context?): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun nullprotect(value: String): String {
        if (value == null) return ""
        else return value
    }

}