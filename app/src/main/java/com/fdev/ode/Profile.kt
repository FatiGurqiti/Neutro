package com.fdev.ode

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
    private var Deletebutton: Button? =null
    private var DontDeletebutton: Button? =null
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val username = findViewById<TextView>(R.id.username)
        val usermail = findViewById<TextView>(R.id.userMail)
        val logo = findViewById<ImageView>(R.id.logoinProfile)
        val scrollLayout = findViewById<RelativeLayout>(R.id.Scroll_Relative)
        BlackFilter    =findViewById(R.id.blackfilterinProfile)
        AreYouSureCard=findViewById(R.id.deleteContactCard)
        Deletebutton=findViewById(R.id.deleteContactBtn)
        DontDeletebutton=findViewById(R.id.notDeleteContactBtn)




        if (user != null) {
            usermail.text = user.email
        }

        //Set Username

        db.collection("Users").document(user?.email.toString()).get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    username.setText( document.getString("username"))
                }
            }


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

                    for (i in 0..myContact.size - 1) {
                        var j = i + 1;

                        val sizeheight = getScreenHeight(this) * 0.5
                        val sizewidth = getScreenWidth(this)

                        val face = resources.getFont(R.font.plusjakartatextregular)
                        val boldface = resources.getFont(R.font.plusjakartatexbold)

                        val Contact_Name = TextView(this)
                        Contact_Name.textSize = 20f
                        Contact_Name.text = ContactNames.get(i)
                        Contact_Name.setTypeface(boldface)
                        scrollLayout.addView(Contact_Name)
                        setMargins( Contact_Name,  (sizewidth * 0.1).toInt(), ((j * sizeheight) * 0.2).toInt(), 25, 1)

                        val Contact_Mail = TextView(this)
                        Contact_Mail.textSize = 20f
                        Contact_Mail.text = myContact.get(i)
                        Contact_Mail.setTypeface(face)
                        scrollLayout.addView(Contact_Mail)
                        setMargins( Contact_Mail, (sizewidth * 0.1).toInt(), (((j * sizeheight) * 0.2) + 100).toInt(), 25, 1)

                        val Delete = ImageButton(this)
                        Delete.setImageResource(R.drawable.delete);
                        Delete.setBackgroundColor(Color.TRANSPARENT)
                        scrollLayout.addView(Delete)
                        setMargins( Delete, (sizewidth * 0.8).toInt(), (((j * sizeheight) * 0.2)).toInt(), 0, 0)


                        Delete.setOnClickListener()
                        {
                            BlackFilter?.visibility = View.VISIBLE
                            AreYouSureCard?.visibility = View.VISIBLE
                        }

                        DontDeletebutton?.setOnClickListener()
                        {
                            BlackFilter?.visibility = View.INVISIBLE
                            AreYouSureCard?.visibility = View.INVISIBLE
                        }

                        Deletebutton?.setOnClickListener(){
                            deleteContact(myContact,myContact.get(i).toString(),ContactNames,ContactNames.get(i).toString())
                        }

                    }

                } else {
                    Log.d(TAG, "No such document")

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }



        logo.setOnClickListener() {
            Toast.makeText(this, "Cleveeer", Toast.LENGTH_SHORT).show()
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", user?.email.toString())
            clipboardManager.setPrimaryClip(clipData)
        }

    }

    private fun deleteContact(myContact: ArrayList<String?>,contactMailToDelete: String, contactNames: ArrayList<String?>,contactNameToDelete: String) {

        myContact.remove(contactMailToDelete) //delete mail adress
        contactNames.remove(contactNameToDelete) // delete name


        //Update the data with new ArrayList
        db.collection("Contacts").document(user?.email.toString())
            .update("contact", myContact)

        db.collection("Contacts").document(user?.email.toString())
            .update("contactName", contactNames)

        //Refresh page
        finish();
        overridePendingTransition(0, 0)
        startActivity(getIntent())
        overridePendingTransition(0, 0)

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

}