package com.fdev.ode.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

public class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {


    private var mContext: Context? = null
    fun FragmentAdapter(context: Context?, fm: FragmentManager?) {
        mContext = context
    }

    override fun createFragment(position: Int): Fragment {

        when (position) {
            1 -> return ToCollect_Fragment()

            2 -> return Debts_Fragment()


            else -> {
                return Debts_Fragment()
            }
        }

    }

    override fun getItemCount(): Int {
        return 2
    }
}