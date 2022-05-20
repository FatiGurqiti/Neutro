package com.fdev.ode.flow.notifications.fragments.debt

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fdev.ode.R

class DebtRequests : Fragment() {

    companion object {
        fun newInstance() = DebtRequests()
    }

    private lateinit var viewModel: DebtRequestsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.debt_requests_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[DebtRequestsViewModel::class.java]
        // TODO: Use the ViewModel
    }

}