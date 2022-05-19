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
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_recivement_.*


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
            baseClass.disableViews(listOf(profileBtn, addDebtBtn, notificationsBtn))

            if (TextUtils.isEmpty(contactName?.text.toString()) ||
                TextUtils.isEmpty(amountText?.text.toString()) ||
                TextUtils.isEmpty(labelText?.text.toString())
            ) {
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
                    contactMail?.text.toString(),
                    contactName?.text.toString(),
                    labelText?.text.toString(),
                    amountText?.text.toString().toDouble(),
                    "Recivements"
                )

                viewModel.getContactUserName(
                    generatedID,
                    contactMail?.text.toString(),
                    labelText?.text.toString(),
                    amountText?.text.toString()
                )

                emptyInputs()
                contactBtn.isEnabled = true
                addDebtBtn.isEnabled = true
                mainActivityProgressBar?.visibility = View.INVISIBLE
                refresh()
            }
        }

        profileBtn.setOnClickListener {
            goto(Profile::class.java)
        }
        notificationsBtn.setOnClickListener {
            goto(Notifications::class.java)
        }

        cancelContactList.setOnClickListener {
            baseClass.enableViews(
                listOf(
                    profileBtn,
                    notificationsBtn,
                    addDebtBtn,
                    cancelContact,
                    cancelDebtCard,
                    addDebtBtnInCard
                )
            )
            secondaryBlackFilter?.visibility = View.INVISIBLE
            contactListCard?.visibility = View.INVISIBLE
            setTextStatus(true)
        }
        cancelContactList.translationZ = 22F

        addDebtBtn.setOnClickListener {
            baseClass.disableViews(listOf(profileBtn, addDebtBtn, notificationsBtn))
            blackFilter.visibility = View.VISIBLE
            addDebtCard.visibility = View.VISIBLE
            contactBtn.isEnabled = false
        }

        dropDownBtn.setOnClickListener {
            secondaryBlackFilter?.visibility = View.VISIBLE
            baseClass.disableViews(listOf(cancelContact, cancelDebtCard, addDebtBtnInCard))
            contactListCard?.visibility = View.VISIBLE
            setTextStatus(false)
        }

        cancelDebtCard.setOnClickListener {
            baseClass.enableViews(listOf(profileBtn, addDebtBtn, notificationsBtn))
            blackFilter.visibility = View.INVISIBLE
            emptyInputs()
            contactBtn.isEnabled = true
            addDebtCard.visibility = View.INVISIBLE
        }

        contactBtn.setOnClickListener {
            baseClass.disableViews(listOf(profileBtn, addDebtBtn, notificationsBtn))
            blackFilter.visibility = View.VISIBLE
            addContactCard.visibility = View.VISIBLE
        }

        addContactBtn.setOnClickListener()
        {
            val contactMail = addContactMail.text.toString()
            val userMail = user?.email.toString()

            if (TextUtils.isEmpty(contactMail)) toast.somethingIsMissing(applicationContext)
            else {
                mainActivityProgressBar?.visibility = View.VISIBLE
                //Add this to your Contact
                if (contactMail != userMail) {
                    viewModel.ifContactExist(contactMail, applicationContext)

                    viewModel.closeContactCard.observe(this, Observer {
                        if (it) {
                            blackFilter.visibility = View.INVISIBLE
                            addContactCard.visibility = View.INVISIBLE
                            addContactMail.setText("")
                            addDebtBtn.isEnabled = true
                            mainActivityProgressBar?.visibility = View.INVISIBLE
                        }
                    })
                    viewModel.refresh.observe(this, Observer {
                        if (it) refresh()
                    })
                } else toast.cantAddYourself(applicationContext)
            }
        }

        cancelContact.setOnClickListener {
            mainActivityProgressBar?.visibility = View.VISIBLE
            baseClass.enableViews(listOf(profileBtn, addDebtBtn, notificationsBtn))
            blackFilter.visibility = View.INVISIBLE
            addContactCard.visibility = View.INVISIBLE
            addContactMail.setText("")
            addDebtBtn.isEnabled = true
            mainActivityProgressBar?.visibility = View.INVISIBLE
        }
    }

    private fun setTextStatus(status: Boolean) {
        amountText?.isEnabled = status
        labelText?.isEnabled = status
    }


    fun setContactNameAndMail(name: String, mail: String) {
        secondaryBlackFilter?.visibility = View.INVISIBLE
        contactListCard?.visibility = View.INVISIBLE
        contactName?.setText(name)
        contactMail?.setText(mail)
        baseClass.enableViews(listOf(cancelContact, cancelDebtCard, addDebtBtnInCard))
        amountText?.isEnabled = true
        labelText?.isEnabled = true
    }

    private fun emptyInputs() {
        contactName?.setText("")
        contactMail?.setText("")
        amountText?.setText("")
        labelText?.setText("")
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

