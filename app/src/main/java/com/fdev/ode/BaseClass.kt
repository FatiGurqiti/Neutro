package com.fdev.ode

import android.content.Context
import android.net.ConnectivityManager


class BaseClass {

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null)
            return true

        return false
    }

}