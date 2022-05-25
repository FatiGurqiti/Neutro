package com.fdev.ode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fdev.ode.services.FirebaseService
import com.fdev.ode.services.NotificationData
import com.fdev.ode.services.PushNotificationData
import com.fdev.ode.services.RetrofitInstance
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic"

class StartActivity : AppCompatActivity() {

    val TAG = "PushNotification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        FirebaseService.sharedPref = getSharedPreferences("sharedPref", MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FirebaseService.token = it.toString()
        }
        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
        buttonSend.setOnClickListener {
            val title = "my title"
            val message = "my message"
            val recipientToken = FirebaseService.token.toString()
            PushNotificationData(
                NotificationData(title, message),
                recipientToken
            ).also {
                sendNotification(it)
            }
        }
    }

    private fun sendNotification(notification: PushNotificationData) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (!response.isSuccessful)
                    Log.e(TAG, response.errorBody().toString())

            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
}