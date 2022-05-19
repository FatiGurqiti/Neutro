package com.fdev.ode.util

import android.content.Context
import android.widget.Toast

class Toasts {

    fun emailSent(context: Context){
        val toast = Toast.makeText(
            context,
            "An email is sent to your mailbox",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun noSuchUser(context: Context){
        val toast = Toast.makeText(
            context,
            "No such user",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun checkInternet(context: Context){
        val toast = Toast.makeText(
            context,
            "Please check your internet connection",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun fillInputs(context: Context){
        val toast = Toast.makeText(
            context,
            "Mind if you fill the inputs?",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun authFail(context: Context){
        val toast = Toast.makeText(
            context,
            "Authentication failed",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun somethingIsMissing(context: Context){
        val toast = Toast.makeText(
            context,
            "Something is missing",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun debtAdded(context: Context){
        val toast = Toast.makeText(
            context,
            "Debt added successfully",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun contactAdded(context: Context){
        val toast = Toast.makeText(
            context,
            "Contact added successfully",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun cantAddYourself(context: Context){
        val toast = Toast.makeText(
            context,
            "I'm sorry. You can't add yourself",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun clever(context: Context){
        val toast = Toast.makeText(
            context,
            "Cleveeer",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun unexpectedError(context: Context){
        val toast = Toast.makeText(
            context,
            "Unexpected Error",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun pinCharacters(context: Context){
        val toast = Toast.makeText(
            context,
            "Pin should be at least 6 characters",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun userExists(context: Context){
        val toast = Toast.makeText(
            context,
            "This user is already exists",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }

    fun accountCreated(context: Context){
        val toast = Toast.makeText(
            context,
            "Account created successfully",
            Toast.LENGTH_SHORT
        )
        toast.show()
    }
}