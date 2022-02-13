package com.fdev.ode

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.fdev.ode.fragments.FragmentAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val user = Firebase.auth.currentUser


    private val db = Firebase.firestore
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

        val tab = findViewById<TabLayout>(R.id.tab)
        val viewpager2 = findViewById<ViewPager2>(R.id.viewPager2)
        val fragmentadapter: FragmentAdapter


        var fm: FragmentManager = supportFragmentManager
        fragmentadapter = FragmentAdapter(fm, lifecycle)
        viewpager2.adapter = fragmentadapter


        tab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                viewpager2.setCurrentItem(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


        viewpager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tab.selectTab(tab.getTabAt(position))
            }
        })

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

                progressBar.visibility = View.VISIBLE
                //Add this to your Contact
                if (odeNO != user?.email.toString()) {

                    contactCheck(odeNO)
                    blackfilter.visibility = View.INVISIBLE
                    contactCard.visibility = View.INVISIBLE
                    odenumber.setText("")
                    debtbtn.isEnabled = true;
                    progressBar.visibility = View.INVISIBLE
                } else
                    Toast.makeText(
                        this, "I'm sorry. You can't add yourself",
                        Toast.LENGTH_SHORT
                    )
                        .show()
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

    private fun contactCheck(Email: String) {

        val TAG = "AddContact"
        val docRef = db.collection("Users").document(Email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    //This user exists
                    contactAdd(Email)
                    Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show()

                } else {
                    Log.d(TAG, "No such document")
                    Toast.makeText(this, "No Such user is found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
                Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun contactAdd(email: String) {

        var myContact = ArrayList<String?>()
        var ContactNames = ArrayList<String?>()

        val TAG = "AddNewContact"
        val docRef: DocumentReference = db.collection("Contacts").document(
            user!!.email.toString()
        )
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    //Get previous Contacts
                    Log.d(
                        TAG,
                        "DocumentSnapshot data: ${document.get("contact") as ArrayList<String>}"
                    )
                    myContact = document.get("contact") as ArrayList<String?>
                    Log.d(TAG, "myContact: ${myContact}")
                    myContact.add(email)
                    updateContact(myContact, email)


                } else {
                    Log.d(TAG, "No such document")

                    // There is no previus data. So, simply add the current one
                    myContact.add(email)
                    addFreshData(myContact, email)
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }


    }

    private fun updateContact(myContact: ArrayList<String?>, email: String) {

        //  Update contact
        db.collection("Contacts").document(user?.email.toString())
            .update("contact", myContact)

        var ContactNames = ArrayList<String?>()
        val TAG = "updateContact"

        val docRef = db.collection("Contacts").document(user?.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Get the old data
                    Log.d(TAG, "DocumentSnapshot data: ${document.get("username")}")
                    ContactNames = document.get("contactName") as ArrayList<String?>

                    db.collection("Users")
                        .document(email)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.data != null) {
                                //Get name of the current contanct
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot data: ${document.getString("username")}"
                                )
                                ContactNames.add(document.getString("username"))

                                //Update the data
                                db.collection("Contacts").document(user?.email.toString())
                                    .update("contactName", ContactNames)
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


    private fun addFreshData(myContact: ArrayList<String?>, Email: String) {

        var ContactNames = ArrayList<String?>()
        val TAG = "AddFreshData"
        val docRef = db.collection("Users").document(Email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Update username
                    Log.d(TAG, "DocumentSnapshot data: ${document.get("username")}")
                    ContactNames.add(document.getString("username"))

                    val contacthash = hashMapOf(
                        "contact" to myContact,
                        "contactName" to ContactNames,
                        "user" to user?.email.toString()
                    )
                    db.collection("Contacts").document(user?.email.toString())
                        .set(contacthash)

                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }


    }

    override fun onBackPressed() {
        //Do nothing when back button is clicked
    }


}


