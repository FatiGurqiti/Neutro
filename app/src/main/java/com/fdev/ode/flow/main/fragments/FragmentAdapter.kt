package com.fdev.ode.flow.main.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fdev.ode.flow.main.fragments.Debts.Debts_Fragment
import com.fdev.ode.flow.main.fragments.Receivements.Recivements_Fragment

class FragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            1 -> Recivements_Fragment()
            2 -> Debts_Fragment()
            else -> Debts_Fragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}