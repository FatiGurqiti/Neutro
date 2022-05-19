package com.fdev.ode.flow.main

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.fdev.ode.BaseClass
import com.fdev.ode.R
import com.fdev.ode.flow.Notifications
import com.fdev.ode.flow.fragments.FragmentAdapter
import com.fdev.ode.flow.profile.Profile
import com.fdev.ode.util.Toasts
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val baseClass = BaseClass()
    private val user = Firebase.auth.currentUser
    lateinit var viewModel: MainViewModel
    private val toast = Toasts()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        val fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)
        viewPager2.adapter = fragmentAdapter
        viewModel.getDebtOrRecievement("Debts", totalText, debtText)
        viewModel.loadContacts(debtsContactList, this, resources)

        profileBtn.setOnClickListener {
            goto(Profile::class.java)
        }
        notificationsBtn.setOnClickListener {
            goto(Notifications::class.java)
        }

        tab.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager2.currentItem = tab.position

                if (tab.position == 0)
                    viewModel.getDebtOrRecievement("Debts", totalText, debtText)
                else
                    viewModel.getDebtOrRecievement("Recivements", totalText, debtText)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })


        viewPager2.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tab.selectTab(tab.getTabAt(position))
            }
        })

        addDebtBtnInCard.setOnClickListener {
            baseClass.setViewsDisabled(listOf(profileBtn, addDebtBtn))

            val contact = contactName?.text.toString()
            val contactMailTxt = contactMail?.text.toString()
            val amount = amountText?.text.toString()
            val label = labelText?.text.toString()

            if (TextUtils.isEmpty(contact) || TextUtils.isEmpty(amount) || TextUtils.isEmpty(label)) {//Input is null
                toast.somethingIsMissing(applicationContext)
            } else {

                toast.debtAdded(applicationContext)
                mainActivityProgressBar?.visibility = View.VISIBLE
                blackFilter.visibility = View.INVISIBLE
                addDebtCard.visibility = View.INVISIBLE

                val generatedID = viewModel.generateId()

                //Update Debt Table
                viewModel.addDebtOrReceivement(
                    generatedID,
                    contactMailTxt,
                    contact,
                    label,
                    amount.toDouble(),
                    "Recivements"
                ) //Add recievement

                viewModel.getContactUserName(generatedID, contactMailTxt, label, amount)

                contactName?.setText("")
                contactMail?.setText("")
                amountText?.setText("")
                labelText?.setText("")
                contactBtn.isEnabled = true
                addDebtBtn.isEnabled = true
                mainActivityProgressBar?.visibility = View.INVISIBLE
                refresh()
            }
        }

        cancelContactList.setOnClickListener {
            mainActivityProgressBar?.visibility = View.VISIBLE
            baseClass.setViewsEnabled(listOf(profileBtn, addDebtBtn))
            secondaryBlackFilter?.visibility = View.INVISIBLE
            contactListCard?.visibility = View.INVISIBLE
            amountText?.isEnabled = true
            labelText?.isEnabled = true
            mainActivityProgressBar?.visibility = View.INVISIBLE
        }
        cancelContactList.translationZ = 22F

        addDebtBtn.setOnClickListener {
            mainActivityProgressBar?.visibility = View.VISIBLE
            baseClass.setViewsDisabled(listOf(profileBtn, addDebtBtn))
            blackFilter.visibility = View.VISIBLE
            addDebtCard.visibility = View.VISIBLE
            contactBtn.isEnabled = false
            mainActivityProgressBar?.visibility = View.INVISIBLE
        }

        dropDownBtn.setOnClickListener {
            secondaryBlackFilter?.visibility = View.VISIBLE
            contactListCard?.visibility = View.VISIBLE
            amountText?.isEnabled = false
            labelText?.isEnabled = false
        }

        cancelDebtCard.setOnClickListener() {
            mainActivityProgressBar?.visibility = View.VISIBLE
            baseClass.setViewsEnabled(listOf(profileBtn, addDebtBtn))
            blackFilter.visibility = View.INVISIBLE
            contactName?.setText("")
            contactMail?.setText("")
            amountText?.setText("")
            labelText?.setText("")
            contactBtn.isEnabled = true
            addDebtCard.visibility = View.INVISIBLE
            mainActivityProgressBar?.visibility = View.INVISIBLE
        }

        contactBtn.setOnClickListener {
            mainActivityProgressBar?.visibility = View.VISIBLE
            baseClass.setViewsDisabled(listOf(profileBtn, addDebtBtn))
            blackFilter.visibility = View.VISIBLE
            addContactCard.visibility = View.VISIBLE
            addDebtBtn.isEnabled = false
            mainActivityProgressBar?.visibility = View.INVISIBLE
        }

        addContactBtn.setOnClickListener()
        {
            val neutroNo = addNeutroNumber.text.toString()
            val userMail = user?.email.toString()


            if (TextUtils.isEmpty(neutroNo)) toast.somethingIsMissing(applicationContext)
            else {
                mainActivityProgressBar?.visibility = View.VISIBLE
                //Add this to your Contact
                if (neutroNo != userMail) {
                    viewModel.ifContactExist(neutroNo, applicationContext)

                    blackFilter.visibility = View.INVISIBLE
                    addContactCard.visibility = View.INVISIBLE
                    addNeutroNumber.setText("")
                    addDebtBtn.isEnabled = true
                    mainActivityProgressBar?.visibility = View.INVISIBLE
                    viewModel.refresh.observe(this, Observer {
                        if (it) refresh()
                    })
                } else toast.cantAddYourself(applicationContext)
            }
        }

        cancelContact.setOnClickListener {
            mainActivityProgressBar?.visibility = View.VISIBLE
            baseClass.setViewsEnabled(listOf(profileBtn, addDebtBtn))
            blackFilter.visibility = View.INVISIBLE
            addContactCard.visibility = View.INVISIBLE
            addNeutroNumber.setText("")
            addDebtBtn.isEnabled = true
            mainActivityProgressBar?.visibility = View.INVISIBLE
        }
    }

    fun setContactNameAndMail(name: String, mail: String) {
        mainActivityProgressBar?.visibility = View.VISIBLE
        secondaryBlackFilter?.visibility = View.INVISIBLE
        contactListCard?.visibility = View.INVISIBLE
        contactName?.setText(name)
        contactMail?.setText(mail)
        amountText?.isEnabled = true
        labelText?.isEnabled = true
        mainActivityProgressBar?.visibility = View.INVISIBLE
    }

    private fun refresh() {
        finish()
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    private fun goto(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
    }

    override fun onBackPressed() {
        //Literally NOTHING!
    }
}

