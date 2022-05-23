package com.fdev.ode.flow.fragments

import android.app.Activity
import android.graphics.Color
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModel
import com.fdev.ode.BaseClass
import com.fdev.ode.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_debts_.*

class DebtsViewModel:ViewModel() {
    
    private val baseClass = BaseClass()
    var totalDebt: Double = 0.0
    
    fun loadDebts(activity: Activity, debtsContactList: RelativeLayout){
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        val docRef = db.collection("Debts").document(user?.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {
                    val amount = document?.get("amount") as ArrayList<Long?>
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
                            val sizeHeight = baseClass.getScreenHeight(activity)
                            val sizeWidth = baseClass.getScreenWidth(activity) * 0.7
                            val font = activity.resources.getFont(R.font.plusjakartatextregular)
                            val boldfont = activity.resources.getFont(R.font.plusjakartatexbold)

                            val card = activity.baseContext?.let { CardView(it) }
                            card!!.layoutParams = RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                            card.radius = 18F
                            card.setContentPadding(25, 25, 25, 25)
                            card.setCardBackgroundColor(Color.parseColor("#1dacd6"))
                            card.resources.getDrawable(R.drawable.black_background)
                            card.cardElevation = 8F
                            card.maxCardElevation = 12F
                            baseClass.setMargins(
                                card,
                                (sizeWidth * .1).toInt(),
                                ((i * sizeHeight) * 0.3).toInt(),
                                (sizeWidth * .1).toInt(),
                                0,
                            )
                            debtsContactList.addView(card)

                            val nameView = TextView(activity.baseContext)
                            baseClass.setNameView(nameView, name[i].toString(),sizeWidth,boldfont)
                            card.addView(nameView)

                            val timeView = TextView(activity.baseContext)
                            baseClass.setDateView(timeView, time[i].toString(), font)
                            card.addView(timeView)

                            val labelView = TextView(activity.baseContext)
                            baseClass.setLabelView(labelView, label[i].toString(), (sizeHeight * .07).toInt(), font)
                            card.addView(labelView)

                            val amountView = TextView(activity.baseContext)
                            baseClass.setAmountView(amountView, amount[i].toString(),(sizeHeight * .15).toInt(),boldfont)
                            card.addView(amountView)
                        }
                    }
                }
            }
    }
}