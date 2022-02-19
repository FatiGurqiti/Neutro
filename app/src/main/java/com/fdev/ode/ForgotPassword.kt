package com.fdev.ode

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        val afterText = findViewById<View>(R.id.aftertext) as TextView
        val reset = findViewById<View>(R.id.reset) as Button
        val resetpassword = findViewById<View>(R.id.emailreset) as EditText
        val pb = findViewById<View>(R.id.progressBar) as ProgressBar


        pb.visibility = View.INVISIBLE
        afterText.visibility = View.INVISIBLE

        reset.setOnClickListener {
            pb.visibility = View.VISIBLE
            resetpassword.visibility = View.INVISIBLE
            reset.isEnabled = false
            afterText.visibility = View.VISIBLE
            pb.visibility = View.INVISIBLE
            val password = resetpassword.text.toString()
            FirebaseAuth.getInstance().sendPasswordResetEmail(password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("EmailStatus", "Email sent.")
                    } else {
                        Log.d("EmailStatus", "Email isn't sent")
                        afterText.text = "No such user"
                    }
                }
        }
    }
}