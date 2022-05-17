package com.fdev.ode

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.fdev.ode.fragments.FragmentAdapter
import com.fdev.ode.util.DebtController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private val baseClass = BaseClass()
    private val user = Firebase.auth.currentUser
    private val db = Firebase.firestore
    private val debtController = DebtController()

    private var contactName: EditText? = null
    private var contactMail: EditText? = null
    private var progressBar: ProgressBar? = null
    private var secondaryBlackFilter: ImageView? = null
    private var contactListCard: CardView? = null
    private var amountText: EditText? = null
    private var labelText: EditText? = null
    private var totalText: TextView? = null
    private var totalAmount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val profile = findViewById<ImageButton>(R.id.profileBtn)
        val blackFilter = findViewById<ImageView>(R.id.blackFilter)
        val contactCard = findViewById<CardView>(R.id.addContactCard)
        val contactButton = findViewById<ImageButton>(R.id.contactBtn)
        val cancelContact = findViewById<ImageButton>(R.id.cancelContact)
        val addContactButton = findViewById<Button>(R.id.addContactBtn)
        val addDebtBtn = findViewById<Button>(R.id.addDebtBtnInCard)
        val debtBtn = findViewById<ImageButton>(R.id.addDebtBtn)
        val debtCard = findViewById<CardView>(R.id.addDebtCard)
        val cancelDebtCard = findViewById<ImageButton>(R.id.cancelDebtCard)
        val cancelContactList = findViewById<ImageButton>(R.id.cancelContactList)
        val dropDown = findViewById<ImageButton>(R.id.dropDownBtn)
        val neutroNr = findViewById<EditText>(R.id.addNeutroNumber)

        contactListCard = findViewById(R.id.contactListCard)
        contactName = findViewById(R.id.contactName)
        contactMail = findViewById(R.id.contactMail)
        amountText = findViewById(R.id.amountText)
        labelText = findViewById(R.id.labelText)
        totalText = findViewById(R.id.totalText)
        totalAmount = findViewById(R.id.debtText)
        secondaryBlackFilter = findViewById(R.id.secondaryBlackFilter)
        progressBar = findViewById(R.id.mainActivityProgressBar)

        val tab = findViewById<TabLayout>(R.id.tab)
        val viewpager2 = findViewById<ViewPager2>(R.id.viewPager2)
        val fragmentAdapter: FragmentAdapter

        val fm: FragmentManager = supportFragmentManager
        fragmentAdapter = FragmentAdapter(fm, lifecycle)
        viewpager2.adapter = fragmentAdapter

        getDebtOrRecievement("Debts") //load debt amount by default
        loadContacts()

        //On Fragment change
        tab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager2.currentItem = tab.position

                if (tab.position == 0)
                    getDebtOrRecievement("Debts")
                else
                    getDebtOrRecievement("Recivements")
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


        viewpager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tab.selectTab(tab.getTabAt(position))
            }
        })

        addDebtBtn.setOnClickListener {
            baseClass.setViewsDisabled(listOf(profile, debtBtn))

            val contact = contactName?.text.toString()
            val contactMailTxt = contactMail?.text.toString()
            val amount = amountText?.text.toString()
            val label = labelText?.text.toString()

            if (TextUtils.isEmpty(contact) || TextUtils.isEmpty(amount) || TextUtils.isEmpty(label)) {//Input is null
                Toast.makeText(this, "Something is missing", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(this, "Debt added succesfully", Toast.LENGTH_SHORT)
                    .show()

                progressBar?.visibility = View.VISIBLE
                blackFilter.visibility = View.INVISIBLE
                debtCard.visibility = View.INVISIBLE

                val generatedID = debtController.GenerateID()

                //Update Debt Table
                debtController.AddDebtAndReceivement(
                    generatedID,
                    contactMailTxt,
                    contact,
                    label,
                    amount.toDouble(),
                    "Recivements"
                ) //Add recievement

                //Get Username of contact
                db.collection("Users").document(user?.email.toString()).get()
                    .addOnSuccessListener { document ->
                        if (document.data != null) {
                            debtController.AddDebtAndReceivement(
                                generatedID,
                                contactMailTxt,
                                document.getString("username").toString(),
                                label,
                                amount.toDouble(),
                                "Debts"
                            ) //Add debt
                        }
                    }

                contactName?.setText("")
                contactMail?.setText("")
                amountText?.setText("")
                labelText?.setText("")
                contactButton.isEnabled = true
                debtBtn.isEnabled = true
                progressBar?.visibility = View.INVISIBLE
                finish()
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
        }

        cancelContactList.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            baseClass.setViewsEnabled(listOf(profile, debtBtn))
            secondaryBlackFilter?.visibility = View.INVISIBLE
            contactListCard?.visibility = View.INVISIBLE
            amountText?.isEnabled = true
            labelText?.isEnabled = true
            progressBar?.visibility = View.INVISIBLE
        }
        cancelContactList.translationZ = 22F

        debtBtn.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            baseClass.setViewsDisabled(listOf(profile, debtBtn))
            blackFilter.visibility = View.VISIBLE
            debtCard.visibility = View.VISIBLE
            contactButton.isEnabled = false
            progressBar?.visibility = View.INVISIBLE
        }

        dropDown.setOnClickListener {
            secondaryBlackFilter?.visibility = View.VISIBLE
            contactListCard?.visibility = View.VISIBLE
            amountText?.isEnabled = false
            labelText?.isEnabled = false
        }

        cancelDebtCard.setOnClickListener() {
            progressBar?.visibility = View.VISIBLE
            baseClass.setViewsEnabled(listOf(profile, debtBtn))
            blackFilter.visibility = View.INVISIBLE
            contactName?.setText("")
            contactMail?.setText("")
            amountText?.setText("")
            labelText?.setText("")
            contactButton.isEnabled = true
            debtCard.visibility = View.INVISIBLE
            progressBar?.visibility = View.INVISIBLE
        }

        contactButton.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            baseClass.setViewsDisabled(listOf(profile, debtBtn))
            blackFilter.visibility = View.VISIBLE
            contactCard.visibility = View.VISIBLE
            debtBtn.isEnabled = false
            progressBar?.visibility = View.INVISIBLE
        }

        addContactButton.setOnClickListener()
        {
            val neutroNo = neutroNr.text.toString()
            val userMail = user?.email.toString()

            if (TextUtils.isEmpty(neutroNo))
                Toast.makeText(this, "Something is missing", Toast.LENGTH_SHORT).show()
            else {
                progressBar?.visibility = View.VISIBLE
                //Add this to your Contact
                if (neutroNo != userMail) {
                    contactCheck(neutroNo)

                    blackFilter.visibility = View.INVISIBLE
                    contactCard.visibility = View.INVISIBLE
                    neutroNr.setText("")
                    debtBtn.isEnabled = true
                    progressBar?.visibility = View.INVISIBLE

                    //Refresh page
                    finish()
                    startActivity(
                        intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                } else
                    Toast.makeText(
                        this, "I'm sorry. You can't add yourself",
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

        cancelContact.setOnClickListener() {
            progressBar?.visibility = View.VISIBLE
            baseClass.setViewsEnabled(listOf(profile, debtBtn))
            blackFilter.visibility = View.INVISIBLE
            contactCard.visibility = View.INVISIBLE
            neutroNr.setText("")
            debtBtn.isEnabled = true
            progressBar?.visibility = View.INVISIBLE
        }


        profile.setOnClickListener() {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }


    fun getDebtOrRecievement(type: String) {
        if (type == "Debts") totalText?.text = "Total Debt"
        else totalText?.setText("Total Receivement")

        val user = Firebase.auth.currentUser
        val docRef = db.collection(type).document(user!!.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Get Old data
                    val valueArray: ArrayList<Double?> =
                        document.get("amount") as ArrayList<Double?>
                    var value = 0.0
                    // Add new data on top of old data
                    for (i in valueArray.indices)
                        value += valueArray[i]!!.toDouble()
                    totalAmount?.text = value.toString()
                }
            }
    }


    private fun loadContacts() {
        val scrollLayout = findViewById<RelativeLayout>(R.id.debtsContactList)

        //Load Contacts
        val docRef = db.collection("Contacts").document(user?.email.toString())
        var myContact = ArrayList<String?>()
        var contactNames = ArrayList<String?>()
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    myContact = document.get("contact") as ArrayList<String?>
                    contactNames = document.get("contactName") as ArrayList<String?>

                    if (myContact.size != 0) { //User has contacts

                        for (i in myContact.indices) {
                            if (myContact[i] != null && contactNames[i] != null) {

                                val sizeHeight = baseClass.getScreenHeight(this) * 0.5
                                val sizeWidth = baseClass.getScreenWidth(this)
                                val font = resources.getFont(R.font.plusjakartatextregular)
                                val boldFont = resources.getFont(R.font.plusjakartatexbold)

                                val contactNameTxt = TextView(this)
                                contactNameTxt.textSize = 20f
                                contactNameTxt.text = contactNames.get(i).toString()
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
                                    setContactNameAndMail(
                                        contactNames[i].toString(),
                                        myContact[i].toString()
                                    )

                                }

                                val contactMailTxt = TextView(this)
                                contactMailTxt.textSize = 20f
                                contactMailTxt.text = myContact.get(i).toString()
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
                                    setContactNameAndMail(contactNames.get(i)!!, myContact.get(i)!!)
                                }
                            }
                        }
                    }
                }
            }
    }

    private fun contactCheck(email: String) {

        val userMail = user!!.email.toString()
        val docRef = db.collection("Users").document(email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    contactAdd(email, userMail)  // add contact to this user
                    contactAdd(userMail, email) // add this to contact

                    Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show()
                    //Refresh page
                    finish()
                    startActivity(
                        intent,
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                }
            }
    }

    private fun contactAdd(email: String, to: String) {

        var myContact = ArrayList<String?>() //email address
        var contactName = ArrayList<String?>() //contact's name

        val docRef: DocumentReference = db.collection("Contacts").document(to)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    //Get previous Contacts
                    myContact = document.get("contact") as ArrayList<String?>
                    contactName = document.get("contactName") as ArrayList<String?>

                    //Get name of the current contanct
                    db.collection("Users")
                        .document(to)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.data != null) {
                                val username = document.get("username")
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
        //  Update contact
        preventDuplicatedData(myContact)
        db.collection("Contacts").document(to)
            .update("contact", myContact)

        var contactNames = ArrayList<String?>()

        val docRef = db.collection("Contacts").document(to)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Get the old data
                    contactNames = document.get("contactName") as ArrayList<String?>

                    db.collection("Users")
                        .document(email)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.data != null) {
                                //Get name of the current contact
                                contactNames.add(document.getString("username"))
                                preventDuplicatedData(contactNames)

                                //Update the data
                                db.collection("Contacts").document(to)
                                    .update("contactName", contactNames)

                                finish()
                                startActivity(
                                    intent,
                                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                                )
                            }
                        }
                }
            }
    }

    fun preventDuplicatedData(list: ArrayList<String?>): ArrayList<String?> {
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

    private fun addFreshData(myContact: ArrayList<String?>, email: String, to: String) {

        val contactName = ArrayList<String?>()
        val docRef = db.collection("Users").document(email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Update username
                    contactName.add(document.getString("username"))

                    val contactHash = hashMapOf(
                        "contact" to myContact,
                        "contactName" to contactName
                    )
                    db.collection("Contacts").document(to)
                        .set(contactHash)
                }
            }
    }


    fun setContactNameAndMail(name: String, mail: String) {
        progressBar?.visibility = View.VISIBLE
        secondaryBlackFilter?.visibility = View.INVISIBLE
        contactListCard?.visibility = View.INVISIBLE
        contactName?.setText(name)
        contactMail?.setText(mail)
        amountText?.isEnabled = true
        labelText?.isEnabled = true
        progressBar?.visibility = View.INVISIBLE
    }

    override fun onBackPressed() {
        //Do nothing when back button is clicked
    }
}

