package com.fdev.ode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fdev.ode.flow.login.LogIn
import com.fdev.ode.services.FirebaseService
import com.fdev.ode.services.NotificationData
import com.fdev.ode.services.PushNotificationData
import com.fdev.ode.services.RetrofitInstance
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.*

const val TOPIC = "/topics/myTopic"

class StartActivity : AppCompatActivity() {

    val TAG = "PushNotification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val intent = Intent(this, LogIn::class.java)
        startActivity(intent)
    }
}