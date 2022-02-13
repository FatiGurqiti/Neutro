package com.fdev.ode

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList


class Profile : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val username = findViewById<TextView>(R.id.username)
        val usermail = findViewById<TextView>(R.id.userMail)
        val logo = findViewById<ImageView>(R.id.logoinProfile)
        val scrollLayout = findViewById<RelativeLayout>(R.id.Scroll_Relative)
        val user = Firebase.auth.currentUser

        if (user != null) {
            usermail.text = user.email
        }

        val TAG = "ViewContact"
        val db = Firebase.firestore
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
                    Log.d(TAG,"myContact: ${myContact}")
                    
                    Log.d(TAG,myContact.size.toString())

                    for (i in 0..myContact.size-1) {
                        var j = i+1;

                        val sizeheight = getScreenHeight(this) * 0.5
                        val sizewidth = getScreenWidth(this)

                        val face = resources.getFont(R.font.plusjakartatextregular)
                        val boldface = resources.getFont(R.font.plusjakartatexbold)

                        val Contact_Name = TextView(this)
                        Contact_Name.textSize = 20f
                        Contact_Name.text = ContactNames.get(i)
                        Contact_Name.setTypeface(boldface)
                        scrollLayout.addView(Contact_Name)
                        setMargins(  Contact_Name,( sizewidth * 0.1).toInt(),  ((j * sizeheight) * 0.2).toInt(), 25, 1 )

                        val Contact_Mail = TextView(this)
                        Contact_Mail.textSize = 20f
                        Contact_Mail.text = myContact.get(i)
                        Contact_Mail.setTypeface(face)
                        scrollLayout.addView(Contact_Mail)
                        setMargins(  Contact_Mail, ( sizewidth * 0.1).toInt(),  (((j * sizeheight) * 0.2) +50 ).toInt(),25, 1 )
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