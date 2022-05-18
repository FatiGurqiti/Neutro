package com.fdev.ode.flow.profile

import android.app.ActivityOptions
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fdev.ode.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_profile.*


class Profile : AppCompatActivity() {

    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        viewModel.loadContacts(
            resources,
            this,
            scroll_Relative,
            profileBlackFilter,
            deleteContactCard,
            profileLogo,
            notDeleteContactBtn,
            deleteContactBtn
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
        profileLogo.setOnClickListener {
            Toast.makeText(this, "Cleveeer", Toast.LENGTH_SHORT).show()
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", user?.email.toString())
            clipboardManager.setPrimaryClip(clipData)
        }
    }
}