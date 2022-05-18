package com.fdev.ode.flow.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.fdev.ode.BaseClass
import com.fdev.ode.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_debts_.*

class Debts_Fragment : Fragment() {

    private val baseClass = BaseClass()
    var totalDebt: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_debts_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        val docRef = db.collection("Debts").document(user?.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    val amount = document.get("amount") as ArrayList<Long?>
                    val label = document.get("label") as ArrayList<String?>
                    val name = document.get("name") as ArrayList<String?>
                    val time = document.get("time") as ArrayList<String?>

                    for (i in amount.indices) {

                        if (name[i] != null &&
                            label[i] != null &&
                            amount[i] != null &&
                            time[i] != null
                        ) {

                            totalDebt += amount[i]!!.toDouble()
                            val sizeHeight = baseClass.getScreenHeight(requireActivity())
                            val sizeWidth = baseClass.getScreenWidth(requireActivity()) * 0.7
                            val font = resources.getFont(R.font.plusjakartatextregular)
                            val boldface = resources.getFont(R.font.plusjakartatexbold)

                            val Card = context?.let { CardView(it) }
                            Card!!.layoutParams = RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                            Card.radius = 18F
                            Card.setContentPadding(25, 25, 25, 25)
                            Card.setCardBackgroundColor(Color.parseColor("#1dacd6"))
                            Card.resources.getDrawable(R.drawable.black_background)
                            Card.cardElevation = 8F
                            Card.maxCardElevation = 12F
                            baseClass.setMargins(
                                Card,
                                (sizeWidth * .1).toInt(),
                                ((i * sizeHeight) * 0.3).toInt(),
                                (sizeWidth * .1).toInt(),
                                (sizeWidth * .1).toInt(),
                            )
                            debtsContactList.addView(Card)

                            val nameView = TextView(context)
                            nameView.textSize = 25f
                            nameView.text = name.get(i).toString()
                            nameView.layoutParams = RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                            )

                            nameView.gravity = Gravity.CENTER;
                            nameView.typeface = boldface
                            nameView.translationZ = 20F
                            nameView.setTextColor(Color.WHITE)
                            Card.addView(nameView)

                            val amountView = TextView(context)
                            amountView.text = amount[i].toString()
                            amountView.textSize = 35f
                            amountView.setTextColor(Color.WHITE)
                            amountView.gravity = Gravity.START
                            Card.addView(amountView)
                            baseClass.setMargins(
                                amountView,
                                0,
                                (sizeHeight * .07).toInt(),
                                (sizeWidth * .03).toInt(),
                                0
                            )

                            val labelView = TextView(context)
                            labelView.textSize = 20f
                            labelView.text = label[i]
                            labelView.setTextColor(Color.WHITE)
                            labelView.layoutParams = RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                            )
                            labelView.gravity = Gravity.START;
                            labelView.typeface = font
                            labelView.translationZ = 18F
                            baseClass.setMargins(
                                labelView,
                                (sizeWidth * .52).toInt(),
                                (sizeHeight * .07).toInt(),
                                1,
                                1
                            )
                            Card.addView(labelView)

                            val timeView = TextView(context)
                            timeView.textSize = 14f
                            timeView.text = time[i].toString()
                            timeView.setTextColor(Color.WHITE)
                            timeView.layoutParams = RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                            )

                            timeView.gravity = Gravity.CENTER
                            timeView.typeface = font
                            timeView.translationZ = 18F
                            baseClass.setMargins(
                                timeView,
                                (sizeWidth * .07).toInt(),
                                (sizeHeight * .165).toInt(),
                                0,
                                0
                            )
                            Card.addView(timeView)
                        }
                    }
                }
            }
    }
}