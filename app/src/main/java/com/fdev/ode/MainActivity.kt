package com.fdev.ode

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.google.android.material.tabs.TabItem

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val profile = findViewById<ImageButton>(R.id.profilebtn)
        val blackfilter = findViewById<ImageView>(R.id.blackfilter)
        val Secondblackfilter = findViewById<ImageView>(R.id.secondblackfilter)
        val contactCard = findViewById<CardView>(R.id.addcontactCard)
        val ContactBtn = findViewById<ImageButton>(R.id.contactBtn)
        val CancelContact = findViewById<ImageButton>(R.id.cancelContact)
        val AddContactBtn = findViewById<Button>(R.id.addContactBtn)

        val AddDebtButton = findViewById<Button>(R.id.addDebtBtnInCard)
        val debtbtn = findViewById<ImageButton>(R.id.addDebtBtn)
        val debtCard = findViewById<CardView>(R.id.addDebtCard)
        val CanceldebtCard = findViewById<ImageButton>(R.id.cancelDebtCard)
        val ContactlistCard = findViewById<CardView>(R.id.ContactListCard)
        val CancelContactList = findViewById<ImageButton>(R.id.cancelContactList)
        val amountText = findViewById<EditText>(R.id.AmountText)
        val labelText = findViewById<EditText>(R.id.LabelText)
        val dropDown = findViewById<ImageButton>(R.id.dropDownBtn)
        val Contacttext = findViewById<EditText>(R.id.contactText);
        val progressBar = findViewById<ProgressBar>(R.id.progressBarinMainActivity)
        val odenumber = findViewById<EditText>(R.id.AddOdeNumber)



        AddDebtButton.setOnClickListener() {
            var contact = Contacttext.text.toString()
            var ammount = amountText.text.toString()
            var label = labelText.text.toString()

            if (TextUtils.isEmpty(contact) || TextUtils.isEmpty(ammount) || TextUtils.isEmpty(label)) {
                //Input is null
                Toast.makeText(this, "Something is missing", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Debt added succesfully", Toast.LENGTH_SHORT)
                    .show()
                progressBar.visibility = View.VISIBLE
                blackfilter.visibility = View.INVISIBLE
                debtCard.visibility = View.INVISIBLE
                Contacttext.setText("")
                amountText.setText("")
                labelText.setText("")
                ContactBtn.isEnabled = true;
                debtbtn.isEnabled = true;
                progressBar.visibility = View.INVISIBLE
            }
        }

        CancelContactList.setOnClickListener() {
            progressBar.visibility = View.VISIBLE
            Secondblackfilter.visibility = View.INVISIBLE
            ContactlistCard.visibility = View.INVISIBLE
            amountText.isEnabled = true
            labelText.isEnabled = true
            progressBar.visibility = View.INVISIBLE
        }

        debtbtn.setOnClickListener() {
            progressBar.visibility = View.VISIBLE
            blackfilter.visibility = View.VISIBLE
            debtCard.visibility = View.VISIBLE
            ContactBtn.isEnabled = false;
            progressBar.visibility = View.INVISIBLE
        }

        dropDown.setOnClickListener() {
            Contacttext.setText("Tihulu")
            Secondblackfilter.visibility = View.VISIBLE
            ContactlistCard.visibility = View.VISIBLE

            amountText.isEnabled = false
            labelText.isEnabled = false
        }

        CanceldebtCard.setOnClickListener() {
            progressBar.visibility = View.VISIBLE
            blackfilter.visibility = View.INVISIBLE
            Contacttext.setText("")
            amountText.setText("")
            labelText.setText("")
            ContactBtn.isEnabled = true;
            debtCard.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
        }

        ContactBtn.setOnClickListener() {
            progressBar.visibility = View.VISIBLE
            blackfilter.visibility = View.VISIBLE
            contactCard.visibility = View.VISIBLE
            debtbtn.isEnabled = false;
            progressBar.visibility = View.INVISIBLE
        }

        AddContactBtn.setOnClickListener()
        {

            var odeNO = odenumber.text.toString()

            if (TextUtils.isEmpty(odeNO)) {
                //Input is null
                Toast.makeText(this, "Something is missing", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "This dude is added to your contact", Toast.LENGTH_SHORT)
                    .show()
                progressBar.visibility = View.VISIBLE
                blackfilter.visibility = View.INVISIBLE
                contactCard.visibility = View.INVISIBLE
                odenumber.setText("")
                debtbtn.isEnabled = true;
                progressBar.visibility = View.INVISIBLE
            }

        }

        CancelContact.setOnClickListener() {
            progressBar.visibility = View.VISIBLE
            blackfilter.visibility = View.INVISIBLE
            contactCard.visibility = View.INVISIBLE
            odenumber.setText("")
            debtbtn.isEnabled = true;
            progressBar.visibility = View.INVISIBLE
        }


        profile.setOnClickListener() {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }
}