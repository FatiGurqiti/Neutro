package com.fdev.ode.flow.notifications.fragments.debt

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fdev.ode.BaseClass
import com.fdev.ode.R

class DebtRequests : Fragment() {

    companion object {
        fun newInstance() = DebtRequests()
    }

    private lateinit var viewModel: DebtRequestsViewModel
    private val baseClass = BaseClass()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[DebtRequestsViewModel::class.java]
        return inflater.inflate(R.layout.debt_requests_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {

        val sizeHeight = baseClass.getScreenHeight(requireActivity())
        val sizeWidth = baseClass.getScreenWidth(requireActivity()) * 0.7
        val font = resources.getFont(R.font.plusjakartatextregular)
        val boldFont = resources.getFont(R.font.plusjakartatexbold)

        viewModel.debts.observe(viewLifecycleOwner) { request ->
            for (i in request[0].indices) {
                Log.d("ThisIsbig",request[0][i].toString())
            }
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[DebtRequestsViewModel::class.java]
    }

}