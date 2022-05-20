package com.fdev.ode.flow.notifications.fragments.contact

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
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
import com.fdev.ode.flow.SharedViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.contact_requests_fragment.*

class ContactRequests : Fragment() {

    companion object {
        fun newInstance() = ContactRequests()
    }

    private lateinit var viewModel: ContactRequestsViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private val baseClass = BaseClass()
    private val user = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[ContactRequestsViewModel::class.java]
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        return inflater.inflate(R.layout.contact_requests_fragment, container, false)
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

        viewModel.contactMail.observe(viewLifecycleOwner) { request ->
            if (request.isNullOrEmpty()) {
                contactRequestEmpty.visibility = View.VISIBLE
                contactRequestLayout.visibility = View.INVISIBLE
            } else {
                contactRequestEmpty.visibility = View.INVISIBLE
                contactRequestLayout.visibility = View.VISIBLE

                for (i in request.indices) {
                    val cardView = context?.let { card -> CardView(card) }
                    cardView!!.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    cardView.radius = 18F
                    cardView.setContentPadding(25, 25, 25, 25)
                    cardView.setCardBackgroundColor(Color.parseColor(("#407BFF")))
                    cardView.resources.getDrawable(R.drawable.black_background)
                    cardView.cardElevation = 8F
                    cardView.maxCardElevation = 12F
                    contactRequestLayout.addView(cardView)
                    baseClass.setMargins(
                        cardView,
                        (sizeWidth * .1).toInt(),
                        ((i * sizeHeight) * 0.16).toInt(),
                        (sizeWidth * .1).toInt(),
                        (sizeWidth * .16).toInt(),
                    )

                    val nameText = TextView(context)
                    nameText.textSize = 25f
                    viewModel.contactUsername.observe(viewLifecycleOwner)
                    {
                        for (j in it.indices) {
                            if (j == i)
                                nameText?.text = it[j]
                        }
                    }
                    nameText.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                    )

                    nameText.gravity = Gravity.START;
                    nameText.typeface = boldFont
                    nameText.translationZ = 35F
                    nameText.setTextColor(Color.WHITE)
                    baseClass.setMargins(
                        nameText,
                        (sizeWidth * .07).toInt(),
                        (sizeHeight * .015).toInt(),
                        0,
                        (sizeHeight * .01).toInt()
                    )
                    cardView.addView(nameText)

                    val dateText = TextView(context)
                    dateText.textSize = 12f
                    viewModel.contactDate.observe(viewLifecycleOwner)
                    {
                        for (j in it.indices) {
                            if (j == i)
                                dateText.text = it[j]
                        }
                    }
                    dateText.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                    )

                    dateText.gravity = Gravity.START;
                    dateText.typeface = font
                    dateText.translationZ = 12F
                    dateText.setTextColor(Color.WHITE)
                    baseClass.setMargins(
                        dateText,
                        (sizeWidth * .07).toInt(),
                        (sizeHeight * .08).toInt(),
                        0,
                        (sizeHeight * .01).toInt()
                    )
                    cardView.addView(dateText)

                    val approve = ImageButton(context)
                    approve.setImageResource(R.drawable.approve);
                    approve.setBackgroundColor(Color.TRANSPARENT)
                    approve.translationZ = 18F
                    cardView.addView(approve)
                    baseClass.setMargins(
                        approve,
                        (sizeWidth * .45).toInt(),
                        0,
                        0,
                        0
                    )

                    val deny = ImageButton(context)
                    deny.setImageResource(R.drawable.deny);
                    deny.setBackgroundColor(Color.TRANSPARENT)
                    deny.translationZ = 18F
                    cardView.addView(deny)
                    baseClass.setMargins(
                        deny,
                        (sizeWidth * .9).toInt(),
                        0,
                        0,
                        0
                    )

                    val statusText = TextView(context)
                    statusText.textSize = 22f
                    statusText.text = "Approved"
                    statusText.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                    )
                   statusText.gravity = Gravity.END
                   statusText.typeface = boldFont
                   statusText.translationZ = 35F
                   statusText.setTextColor(Color.WHITE)
                    baseClass.setMargins(
                        statusText,
                        (sizeHeight * .01).toInt(),
                        (sizeHeight * .04).toInt(),
                        (sizeHeight * .01).toInt(),
                        (sizeHeight * .01).toInt()
                    )
                    cardView.addView(statusText)

                    statusText.visibility = View.GONE

                    approve.setOnClickListener()
                    {
                        viewModel.approveContact(request[i],user?.email.toString()) //Add to this users contact list
                        viewModel.approveContact(user?.email.toString(),request[i]) //Add to other users contact list
                        viewModel.denyContact(request[i]) //Delete request

                        statusText.setTextColor(Color.GREEN)
                        statusText.text = "Approved"
                        statusText.visibility = View.VISIBLE
                        approve.visibility = View.GONE
                        deny.visibility = View.GONE
                    }

                    deny.setOnClickListener()
                    {
                        viewModel.denyContact(request[i])
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
        viewModel = ViewModelProvider(this)[ContactRequestsViewModel::class.java]
        // TODO: Use the ViewModel
    }

}