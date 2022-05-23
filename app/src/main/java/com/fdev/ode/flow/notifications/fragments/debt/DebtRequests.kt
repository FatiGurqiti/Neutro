package com.fdev.ode.flow.notifications.fragments.debt

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.fdev.ode.BaseClass
import com.fdev.ode.R
import kotlinx.android.synthetic.main.debt_requests_fragment.*

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
            if (request.isNullOrEmpty()) {
                debtRequestEmpty.visibility = View.VISIBLE
                debtRequestLayout.visibility = View.INVISIBLE
            } else {
                debtRequestEmpty.visibility = View.INVISIBLE
                debtRequestLayout.visibility = View.VISIBLE

                for (i in request[0].indices) {
                    val cardView = context?.let { card -> CardView(card) }
                    cardView!!.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    cardView.minimumHeight = sizeHeight / 5
                    cardView.radius = 18F
                    cardView.setContentPadding(25, 25, 25, 25)
                    cardView.setCardBackgroundColor(Color.parseColor(("#407BFF")))
                    cardView.resources.getDrawable(R.drawable.black_background)
                    cardView.cardElevation = 8F
                    cardView.maxCardElevation = 12F
                    debtRequestLayout.addView(cardView)
                    baseClass.setMargins(
                        cardView,
                        (sizeWidth * .1).toInt(),
                        ((i * sizeHeight) * .3).toInt(),
                        (sizeWidth * .1).toInt(),
                        0,
                    )
                    val nameText = TextView(context)
                    nameText.textSize = 25f
                    nameText.text = request[5][i]
                    nameText.gravity = Gravity.CENTER
                    nameText.typeface = boldFont
                    nameText.setTextColor(Color.WHITE)
                    nameText.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                    )
                    baseClass.setMargins(
                        nameText,
                        0,
                        (sizeHeight * .03).toInt(),
                        0,
                        (sizeHeight * .01).toInt()
                    )
                    cardView.addView(nameText)

                    val labelText = TextView(context)
                    labelText.textSize = 17f
                    labelText.text = request[6][i]
                    labelText.gravity = Gravity.START;
                    labelText.typeface = font
                    labelText.setTextColor(Color.WHITE)
                    labelText.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                    )
                    baseClass.setMargins(
                        labelText,
                        (sizeWidth * .07).toInt(),
                        (sizeHeight * .1).toInt(),
                        (sizeWidth * .6).toInt(),
                        (sizeHeight * .01).toInt()
                    )
                    cardView.addView(labelText)

                    val amountText = TextView(context)
                    amountText.textSize = 30f
                    amountText.text = request[0][i]
                    amountText.gravity = Gravity.END
                    amountText.typeface = font
                    amountText.setTextColor(Color.WHITE)
                    amountText.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                    )
                    baseClass.setMargins(
                        amountText,
                        (sizeWidth * .01).toInt(),
                        (sizeHeight * .1).toInt(),
                        (sizeWidth * .1).toInt(),
                        (sizeHeight * .01).toInt()
                    )
                    cardView.addView(amountText)

                    val dateText = TextView(context)
                    dateText.textSize = 12f
                    dateText.text = request[7][i]
                    dateText.gravity = Gravity.CENTER
                    dateText.typeface = font
                    dateText.setTextColor(Color.WHITE)
                    dateText.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                    )
                    cardView.addView(dateText)


                    val approve = ImageButton(context)
                    approve.setImageResource(R.drawable.approve)
                    approve.setBackgroundColor(Color.TRANSPARENT)
                    approve.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                    )
                    cardView.addView(approve)
                    baseClass.setMargins(
                        approve,
                        (sizeWidth * .35).toInt(),
                        (sizeHeight * .18).toInt(),
                        0,
                        0
                    )

                    val deny = ImageButton(context)
                    deny.setImageResource(R.drawable.deny)
                    deny.setBackgroundColor(Color.TRANSPARENT)
                    deny.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                    )
                    cardView.addView(deny)
                    baseClass.setMargins(
                        deny,
                        (sizeWidth * .65).toInt(),
                        (sizeHeight * .185).toInt(),
                        0,
                        0
                    )
                    deny.translationZ = 2f
                    deny.elevation = 2f

                    val statusText = TextView(context)
                    statusText.textSize = 22f
                    statusText.text = "Approved"
                    statusText.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                    )
                    statusText.gravity = Gravity.CENTER
                    statusText.typeface = boldFont
                    statusText.setTextColor(Color.WHITE)
                    baseClass.setMargins(
                        statusText,
                        0,
                        (sizeHeight * .2).toInt(),
                        0, 0
                    )
                    cardView.addView(statusText)
                    statusText.visibility = View.GONE

                    approve.setOnClickListener()
                    {
                        // Add debt to this user
                        viewModel.approveDebt(
                            request[1][i],
                            request[4][i],
                            request[5][i],
                            request[6][i],
                            request[7][i],
                            request[0][i].toDouble(),
                            "Debts",
                            request[2][i]
                        )

                        // Add receivement to user that requests
                        viewModel.approveDebt(
                            request[1][i],
                            request[2][i],
                            request[3][i],
                            request[6][i],
                            request[7][i],
                            request[0][i].toDouble(),
                            "Recivements",
                            request[4][i]
                        )

                        //Delete request
                        viewModel.denyContact(request[1][i])

                        statusText.setTextColor(Color.GREEN)
                        statusText.text = "Approved"
                        statusText.visibility = View.VISIBLE
                        approve.visibility = View.GONE
                        deny.visibility = View.GONE
                    }

                    deny.setOnClickListener()
                    {
                        viewModel.denyContact(request[1][i])
                        statusText.setTextColor(Color.RED)
                        statusText.text = "Denied"
                        statusText.visibility = View.VISIBLE
                        approve.visibility = View.GONE
                        deny.visibility = View.GONE
                    }
                }
            }
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[DebtRequestsViewModel::class.java]
    }

}