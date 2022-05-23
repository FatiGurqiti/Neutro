package com.fdev.ode.flow.main.fragments.Receivements

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fdev.ode.flow.main.MainActivity
import com.fdev.ode.R
import kotlinx.android.synthetic.main.fragment_recivement_.*

class Recivements_Fragment : Fragment() {

    private lateinit var viewModel: ReceivementsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recivement_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ReceivementsViewModel::class.java]
        viewModel.loadReceivements(
            requireActivity(),
            receivementsRelativeLayout,
            receivementsBlackFilter,
            receivementsDeleteDebtCard,
            receivementsNotDeleteDebtBtn,
            receivementsDeleteDebtBtn
        )
        viewModel.refresh.observe(viewLifecycleOwner) {
            if (it) {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.loadReceivements(
            requireActivity(),
            receivementsRelativeLayout,
            receivementsBlackFilter,
            receivementsDeleteDebtCard,
            receivementsNotDeleteDebtBtn,
            receivementsDeleteDebtBtn
        )
        viewModel.refresh.observe(viewLifecycleOwner) {
            if (it) {
                val intent = Intent(context, MainActivity::class.java)
                startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
                )
            }
        }
    }
}