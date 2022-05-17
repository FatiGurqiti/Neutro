package com.fdev.ode.fragments

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

class Debts_Fragment : Fragment() {

    val baseClass = BaseClass()
    lateinit var scrollLayout: RelativeLayout
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
        scrollLayout = view.findViewById(R.id.debtsContactList)

        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        var amount = ArrayList<Long?>()
        var label = ArrayList<String?>()
        var name = ArrayList<String?>()
        var time = ArrayList<String?>()

        val docRef = db.collection("Debts").document(user?.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    amount = document.get("amount") as ArrayList<Long?>
                    label = document.get("label") as ArrayList<String?>
                    name = document.get("name") as ArrayList<String?>
                    time = document.get("time") as ArrayList<String?>

                    for (i in amount.indices) {

                        if (name[i] != null &&
                            label[i] != null &&
                            amount[i] != null &&
                            time[i] != null
                        ) {

                            totalDebt += amount[i]!!.toDouble()

                            val sizeheight = baseClass.getScreenHeight(requireActivity())
                            val sizewidth = baseClass.getScreenWidth(requireActivity()) * 0.7
                            val font = resources.getFont(R.font.plusjakartatextregular)
                            val boldface = resources.getFont(R.font.plusjakartatexbold)

                            val Card = context?.let { CardView(it) }
                            Card!!.setLayoutParams(
                                RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                                )
                            )
                            Card.radius = 18F
                            Card.setContentPadding(25, 25, 25, 25)
                            Card.setCardBackgroundColor(Color.parseColor("#1dacd6"))
                            Card.resources.getDrawable(R.drawable.black_background)
                            Card.cardElevation = 8F
                            Card.maxCardElevation = 12F
                            baseClass.setMargins(
                                Card,
                                (sizewidth * .1).toInt(),
                                ((i * sizeheight) * 0.3).toInt(),
                                (sizewidth * .1).toInt(),
                                (sizewidth * .1).toInt(),
                            )
                            scrollLayout.addView(Card)

                            val Name = TextView(context)
                            Name.textSize = 25f
                            Name.text = name.get(i).toString()
                            Name.setLayoutParams(
                                RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                )
                            )

                            Name.setGravity(Gravity.CENTER);
                            Name.setTypeface(boldface)
                            Name.setTranslationZ(20F)
                            Name.setTextColor(Color.WHITE)
                            Card.addView(Name)

                            val Amount = TextView(context)
                            Amount.text = amount.get(i).toString()
                            Amount.textSize = 35f
                            Amount.setTextColor(Color.WHITE)
                            Amount.setGravity(Gravity.START)
                            Card.addView(Amount)
                            baseClass.setMargins(
                                Amount,
                                0,
                                (sizeheight * .07).toInt(),
                                (sizewidth * .03).toInt(),
                                0
                            )

                            val Label = TextView(context)
                            Label.textSize = 20f
                            Label.text = label.get(i)
                            Label.setTextColor(Color.WHITE)
                            Label.setLayoutParams(
                                RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                )
                            )
                            Label.setGravity(Gravity.START);
                            Label.setTypeface(font)
                            Label.setTranslationZ(18F)
                            baseClass.setMargins(
                                Label,
                                (sizewidth * .52).toInt(),
                                (sizeheight * .07).toInt(),
                                1,
                                1
                            )
                            Card.addView(Label)

                            val Time = TextView(context)
                            Time.textSize = 14f
                            Time.text = time.get(i).toString()
                            Time.setTextColor(Color.WHITE)
                            Time.setLayoutParams(
                                RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                )
                            )

                            Time.setGravity(Gravity.CENTER)
                            Time.setTypeface(font)
                            Time.setTranslationZ(18F)
                            baseClass.setMargins(
                                Time,
                                (sizewidth * .07).toInt(),
                                (sizeheight * .165).toInt(),
                                0,
                                0
                            )
                            Card.addView(Time)
                        }
                    }
                }
            }
    }
}