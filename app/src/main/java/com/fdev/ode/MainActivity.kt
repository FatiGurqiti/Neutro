package com.fdev.ode

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
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

    private var Contactname: EditText? = null
    private var Contactmail: EditText? = null
    private var progressBar: ProgressBar? = null
    private var Secondblackfilter: ImageView? = null
    private var ContactlistCard: CardView? = null
    private var amountText: EditText? = null
    private var labelText: EditText? = null
    private var TotalText: TextView? = null
    private var TotalAmount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TotalAmount = findViewById(R.id.debtText)
        val profile = findViewById<ImageButton>(R.id.profilebtn)
        val blackfilter = findViewById<ImageView>(R.id.blackfilter)
        val contactCard = findViewById<CardView>(R.id.addcontactCard)
        val ContactBtn = findViewById<ImageButton>(R.id.contactBtn)
        val CancelContact = findViewById<ImageButton>(R.id.cancelContact)
        val AddContactBtn = findViewById<Button>(R.id.addContactBtn)

        val AddDebtButton = findViewById<Button>(R.id.addDebtBtnInCard)
        val debtbtn = findViewById<ImageButton>(R.id.addDebtBtn)
        val debtCard = findViewById<CardView>(R.id.addDebtCard)
        val CanceldebtCard = findViewById<ImageButton>(R.id.cancelDebtCard)
        val CancelContactList = findViewById<ImageButton>(R.id.cancelContactList)

        val dropDown = findViewById<ImageButton>(R.id.dropDownBtn)
        val odenumber = findViewById<EditText>(R.id.AddOdeNumber)

        ContactlistCard = findViewById(R.id.ContactListCard)
        Contactname = findViewById(R.id.contactName)
        Contactmail = findViewById(R.id.contactMail)
        amountText = findViewById(R.id.AmountText)
        labelText = findViewById(R.id.LabelText)
        TotalText = findViewById(R.id.totalText)

        Secondblackfilter = findViewById(R.id.secondblackfilter)
        progressBar = findViewById(R.id.progressBarinMainActivity)

        //receivements = findViewById(R.id.Scroll_RelativeofRecivements)

        val tab = findViewById<TabLayout>(R.id.tab)
        val viewpager2 = findViewById<ViewPager2>(R.id.viewPager2)
        val fragmentadapter: FragmentAdapter

        val fm: FragmentManager = supportFragmentManager
        fragmentadapter = FragmentAdapter(fm, lifecycle)
        viewpager2.adapter = fragmentadapter


        GetDebtOrRecivement("Debts") //load debt amount by default
        loadContacts()

        //On Fragment change
        tab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager2.setCurrentItem(tab.position)

                Log.d("Fragment Position", tab.position.toString())

                if(tab.position == 0)
                {
                    Log.d("Fragment", "this is debt")
                    GetDebtOrRecivement("Debts")
                }
                else
                {
                    Log.d("Fragment","this is to collect")
                    GetDebtOrRecivement("Recivements")
                }
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
            baseClass.setViewsDisabled(listOf(profile,debtbtn))
            Log.d("disabled","debt")

            val contact = Contactname?.text.toString()
            val contactmail = Contactmail?.text.toString()
            val amount = amountText?.text.toString()
            val label = labelText?.text.toString()

            if (TextUtils.isEmpty(contact) || TextUtils.isEmpty(amount) || TextUtils.isEmpty(label)) {
                //Input is null
                Toast.makeText(this, "Something is missing", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Debt added succesfully", Toast.LENGTH_SHORT)
                    .show()
                progressBar?.visibility = View.VISIBLE
                blackfilter.visibility = View.INVISIBLE
                debtCard.visibility = View.INVISIBLE

                Log.d("name",contact)
                Log.d("mail",contactmail)
                var ID = debtController.GenerateID()

                //Update Debt Table
                debtController.AddDebtAndReceivement(ID,contactmail,contact,label,amount.toDouble(),"Recivements") //Add recivement

                //Get Username of contact
                db.collection("Users").document(user?.email.toString()).get()
                    .addOnSuccessListener { document ->
                        if (document.data != null) {
                            var name = document.getString("username").toString()
                            debtController.AddDebtAndReceivement(ID,contactmail,name,label,amount.toDouble(),"Debts") //Add debt.
                        }
                    }

                Contactname?.setText("")
                Contactmail?.setText("")
                amountText?.setText("")
                labelText?.setText("")
                ContactBtn.isEnabled = true;
                debtbtn.isEnabled = true;
                progressBar?.visibility = View.INVISIBLE
                finish()
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
        }


        CancelContactList.setOnClickListener() {
            progressBar?.visibility = View.VISIBLE
            baseClass.setViewsEnabled(listOf(profile,debtbtn))
            Secondblackfilter?.visibility = View.INVISIBLE
            ContactlistCard?.visibility = View.INVISIBLE
            amountText?.isEnabled = true
            labelText?.isEnabled = true
            progressBar?.visibility = View.INVISIBLE
        }
        CancelContactList.setTranslationZ(22F)

        debtbtn.setOnClickListener() {
            progressBar?.visibility = View.VISIBLE
            baseClass.setViewsDisabled(listOf(profile,debtbtn))
            blackfilter.visibility = View.VISIBLE
            debtCard.visibility = View.VISIBLE
            ContactBtn.isEnabled = false;
            progressBar?.visibility = View.INVISIBLE
        }

        dropDown.setOnClickListener() {
            Secondblackfilter?.visibility = View.VISIBLE
            ContactlistCard?.visibility = View.VISIBLE
            amountText?.isEnabled = false
            labelText?.isEnabled = false
        }

        CanceldebtCard.setOnClickListener() {
            progressBar?.visibility = View.VISIBLE
            baseClass.setViewsEnabled(listOf(profile,debtbtn))
            blackfilter.visibility = View.INVISIBLE
            Contactname?.setText("")
            Contactmail?.setText("")
            amountText?.setText("")
            labelText?.setText("")
            ContactBtn.isEnabled = true;
            debtCard.visibility = View.INVISIBLE
            progressBar?.visibility = View.INVISIBLE
        }

        ContactBtn.setOnClickListener() {
            progressBar?.visibility = View.VISIBLE
            baseClass.setViewsDisabled(listOf(profile,debtbtn))
            blackfilter.visibility = View.VISIBLE
            contactCard.visibility = View.VISIBLE
            debtbtn.isEnabled = false;
            progressBar?.visibility = View.INVISIBLE
        }

        AddContactBtn.setOnClickListener()
        {
            val odeNO = odenumber.text.toString()
            val userMail =user?.email.toString()

            if (TextUtils.isEmpty(odeNO)) {
                //Input is null
                Toast.makeText(this, "Something is missing", Toast.LENGTH_SHORT).show()
            } else {

                progressBar?.visibility = View.VISIBLE
                //Add this to your Contact
                if (odeNO != userMail) {


                    contactCheck(odeNO)

                    blackfilter.visibility = View.INVISIBLE
                    contactCard.visibility = View.INVISIBLE
                    odenumber.setText("")
                    debtbtn.isEnabled = true;
                    progressBar?.visibility = View.INVISIBLE

                    //Refresh page
                    finish()
                    startActivity(
                        getIntent(),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                    )
                } else
                    Toast.makeText(
                        this, "I'm sorry. You can't add yourself",
                        Toast.LENGTH_SHORT
                    )
                        .show()
            }

        }

        CancelContact.setOnClickListener() {
            progressBar?.visibility = View.VISIBLE
            baseClass.setViewsEnabled(listOf(profile,debtbtn))
            blackfilter.visibility = View.INVISIBLE
            contactCard.visibility = View.INVISIBLE
            odenumber.setText("")
            debtbtn.isEnabled = true;
            progressBar?.visibility = View.INVISIBLE
        }


        profile.setOnClickListener() {
            val intent = Intent(this, Profile::class.java)
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }
    }


    fun GetDebtOrRecivement(type: String) {

        if (type == "Debts") TotalText?.setText("Total Debt")
        else TotalText?.setText("Total Receivement")


        val TAG = "GetDebtOrToCollect"
        val user = Firebase.auth.currentUser
        val docRef = db.collection(type).document(user!!.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    //Get Old data
                    Log.d(TAG, "DocumentSnapshot data: ${document.get("amount")}")
                    val valueArray: ArrayList<Double?>
                    valueArray = document.get("amount") as ArrayList<Double?>
                    var value =0.0
                    for(i in 0..valueArray.size -1)
                    {
                        value += valueArray[i]!!.toDouble()
                    }
                    TotalAmount?.setText(value.toString())

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }

    }


    private fun loadContacts() {
        val scrollLayout = findViewById<RelativeLayout>(R.id.Scroll_RelativeofContactList)

        //Load Contacts
        val TAG = "LoadContact"
        val docRef = db.collection("Contacts").document(user?.email.toString())
        var myContact = ArrayList<String?>()
        var ContactNames = ArrayList<String?>()
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    myContact = document.get("contact") as ArrayList<String?>
                    ContactNames = document.get("contactName") as ArrayList<String?>
                    Log.d(TAG, "myContact: ${myContact}")

                    Log.d(TAG, myContact.size.toString())

                    if (myContact.size != 0) { //User has contacts

                    for (i in myContact.indices ) {
                        if(myContact.get(i) != null && ContactNames.get(i) != null ){

                        val sizeheight = getScreenHeight(this) * 0.5
                        val sizewidth = getScreenWidth(this)

                        val face = resources.getFont(R.font.plusjakartatextregular)
                        val boldface = resources.getFont(R.font.plusjakartatexbold)

                        val Contact_Name = TextView(this)
                        Contact_Name.textSize = 20f
                        Contact_Name.text = ContactNames.get(i).toString()
                        Contact_Name.setLayoutParams(
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                        )
                        Contact_Name.setTypeface(boldface)
                        Contact_Name.setTranslationZ(20F)
                        scrollLayout.addView(Contact_Name)
                        setMargins(
                            Contact_Name,
                            (sizewidth * 0.1).toInt(),
                            ((i * sizeheight) * 0.2).toInt(),
                            0,
                            0
                        )

                        Contact_Name.setOnClickListener()
                        {
                            setContactNameAndMail(ContactNames.get(i).toString(), myContact.get(i).toString())

                        }

                        val Contact_Mail = TextView(this)
                        Contact_Mail.textSize = 20f
                        Contact_Mail.text = myContact.get(i).toString()
                        Contact_Mail.setTypeface(face)
                        Contact_Mail.setTranslationZ(20F)
                        Contact_Mail.setLayoutParams(
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                        )
                        scrollLayout.addView(Contact_Mail)
                        setMargins(
                            Contact_Mail,
                            (sizewidth * 0.1).toInt(),
                            (((i * sizeheight) * 0.2) + ((sizeheight) * 0.08)).toInt(),
                            0,
                            0
                        )

                        Contact_Mail.setOnClickListener() {
                            setContactNameAndMail(ContactNames.get(i)!!, myContact.get(i)!!)
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

    private fun contactCheck(Email: String) {

        val TAG = "AddContact"
        val userMail = user!!.email.toString()
        val docRef = db.collection("Users").document(Email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    //This user exists


                    contactAdd(Email,userMail)  // add contact to this user
                    contactAdd(userMail,Email) // add this to contact


                    Toast.makeText(this, "Contact added successfully", Toast.LENGTH_SHORT).show()
                    //Refresh page
                    finish()
                    startActivity(getIntent(),ActivityOptions.makeSceneTransitionAnimation(this).toBundle())

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

    private fun contactAdd(email: String, to: String) {

        var myContact = ArrayList<String?>() //email address
        var ContactNames = ArrayList<String?>() //contact's name

        val TAG = "AddNewContact"
        val docRef: DocumentReference = db.collection("Contacts").document(to)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    //Get previous Contacts
                    myContact = document.get("contact") as ArrayList<String?>
                    ContactNames = document.get("contactName") as ArrayList<String?>

                    //Get name of the current contanct
                    db.collection("Users")
                        .document(to)
                        .get()
                        .addOnSuccessListener { document ->
                            if (document.data != null) {

                                val username = document.get("username")
                                ContactNames.add(username.toString())

                                myContact.add(email)
                                updateContact(myContact, email,to)

                            }
                        }


                } else {
                    Log.d(TAG, "No such document")

                    // There is no previus data. So, simply add the current one
                    myContact.add(email)
                    addFreshData(myContact, email,to)


                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    private fun updateContact(myContact: ArrayList<String?>, email: String,to: String) {
        //  Update contact
        preventDublicatedData(myContact)
        db.collection("Contacts").document(to)
            .update("contact", myContact)

        var ContactNames = ArrayList<String?>()
        val TAG = "updateContact"

        val docRef = db.collection("Contacts").document(to)
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
                                preventDublicatedData(ContactNames)

                                //Update the data
                                db.collection("Contacts").document(to)
                                    .update("contactName", ContactNames)

                                finish()
                                startActivity(
                                    getIntent(),
                                    ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                                )
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

    fun preventDublicatedData(List: ArrayList<String?>): ArrayList<String?> {
        for (i in 0..List.size) {
            for (j in i + 1..List.size) {
                if (j < List.size) {
                    if (List.get(i) == List.get(j))
                        List.removeAt(j)
                }
            }
        }
        return List
    }

    private fun addFreshData(myContact: ArrayList<String?>, Email: String, to: String) {

        val ContactNames = ArrayList<String?>()
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
                        "contactName" to ContactNames
                    )
                    db.collection("Contacts").document(to)
                        .set(contacthash)

                } else {
                    Log.d(TAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }


    }


    fun setContactNameAndMail(name: String,mail: String) {
        progressBar?.visibility = View.VISIBLE
        Secondblackfilter?.visibility = View.INVISIBLE
        ContactlistCard?.visibility = View.INVISIBLE
        Contactname?.setText(name)
        Contactmail?.setText(mail)
        amountText?.isEnabled = true
        labelText?.isEnabled = true
        progressBar?.visibility = View.INVISIBLE
    }

    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.getLayoutParams() is ViewGroup.MarginLayoutParams) {
            val p = v.getLayoutParams() as ViewGroup.MarginLayoutParams
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




    override fun onBackPressed() {
        //Do nothing when back button is clicked
    }


}

