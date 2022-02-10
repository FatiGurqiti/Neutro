package com.fdev.ode

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val profile = findViewById<ImageButton>(R.id.profilebtn)
        val blackfilter = findViewById<ImageView>(R.id.blackfilter)
        val contactCard = findViewById<CardView>(R.id.addcontactCard)
        val ContactBtn = findViewById<ImageButton>(R.id.contactBtn)
        val AddContactBtn = findViewById<Button>(R.id.addContactBtn)
        val debtbtn = findViewById<ImageButton>(R.id.addDebtBtn)
        val progressBar = findViewById<ProgressBar>(R.id.progressBarinMainActivity)
        val odenumber = findViewById<EditText>(R.id.AddOdeNumber)

        ContactBtn.setOnClickListener() {
            progressBar.visibility = View.VISIBLE
            blackfilter.visibility = View.VISIBLE
            contactCard.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }

        AddContactBtn.setOnClickListener()
        {

            var odeNO = odenumber.text.toString()

            if (TextUtils.isEmpty(odeNO)) {
                //Input is null
                Toast.makeText(this,"Something is missing", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"This dude is added to your contact", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.VISIBLE
                blackfilter.visibility = View.INVISIBLE
                contactCard.visibility = View.INVISIBLE
                progressBar.visibility = View.INVISIBLE
            }

        }


        profile.setOnClickListener() {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }
}