package com.fdev.ode.flow.fragments.Debts

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fdev.ode.R
import kotlinx.android.synthetic.main.fragment_debts_.*

class Debts_Fragment : Fragment() {

    private lateinit var viewModel: DebtsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_debts_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[DebtsViewModel::class.java]
        viewModel.loadDebts(requireActivity(),debtsContactList)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadDebts(requireActivity(),debtsContactList)
    }

}