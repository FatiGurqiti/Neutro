package com.fdev.ode.flow.profile

import android.app.ActivityOptions
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fdev.ode.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class Profile : AppCompatActivity() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        val username = findViewById<TextView>(R.id.username)
        val userMail = findViewById<TextView>(R.id.userMail)

        val logo = findViewById<ImageView>(R.id.profileLogo)
        val scrollLayout = findViewById<RelativeLayout>(R.id.scroll_Relative)
        val blackFilter = findViewById<ImageView>(R.id.profileBlackFilter)
        val areYouSureCard = findViewById<CardView>(R.id.deleteContactCard)
        val deleteButton = findViewById<Button>(R.id.deleteContactBtn)
        val dontDeleteButton = findViewById<Button>(R.id.notDeleteContactBtn)

        viewModel.loadContacts(
            resources,
            this,
            scrollLayout,
            blackFilter,
            areYouSureCard,
            logo,
            dontDeleteButton,
            deleteButton
        )
        val user = Firebase.auth.currentUser
        if (user != null) userMail.text = user.email

        viewModel.setUserName(username)

        viewModel.isDeleted.observe(this, Observer {
            if (it) {
                finish()
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }
        })

        //Copy mail adress
        logo.setOnClickListener {
            Toast.makeText(this, "Cleveeer", Toast.LENGTH_SHORT).show()
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", user?.email.toString())
            clipboardManager.setPrimaryClip(clipData)
        }
    }
}