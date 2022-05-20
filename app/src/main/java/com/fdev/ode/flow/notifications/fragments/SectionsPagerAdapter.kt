package com.fdev.ode.flow.notifications.fragments

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.fdev.ode.R
import com.fdev.ode.flow.notifications.fragments.contact.ContactRequests
import com.fdev.ode.flow.notifications.fragments.debt.DebtRequests

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2
)
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        //This makes no sense at all. But it works
        return when (position) {
            1 -> DebtRequests()
            2 -> DebtRequests()
            else -> ContactRequests()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}