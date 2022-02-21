package com.fdev.ode.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.fdev.ode.MainActivity
import com.fdev.ode.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class Debts_Fragment : Fragment() {

    lateinit var scrollLayout: RelativeLayout
    var totalDebt:Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_debts_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollLayout = view.findViewById(R.id.Scroll_RelativeofContactList)


        val TAG = "LoadDebts"
        val db = Firebase.firestore
        val user = Firebase.auth.currentUser

        var amount = ArrayList<Long?>()
        var label = ArrayList<String?>()
        var name = ArrayList<String?>()
        var mail = ArrayList<String?>()
        var time = ArrayList<String?>()

        val docRef = db.collection("Debts").document(user?.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    Log.d(
                        TAG,
                        "DocumentSnapshot data: ${document.get("amount") as ArrayList<String?>}"
                    )
                    amount = document.get("amount") as ArrayList<Long?>
                    label = document.get("label") as ArrayList<String?>
                    name = document.get("name") as ArrayList<String?>
                    mail = document.get("to") as ArrayList<String?>
                    time = document.get("time") as ArrayList<String?>

                    Log.d(TAG, "amount: ${amount}")

                    Log.d(TAG, amount.size.toString())

                    for (i in 0..amount.size - 1) {

                        if (name.get(i) != null && label.get(i) != null && amount.get(i) != null && time.get(i) != null)
                        {

                        val j = i + 1
                        totalDebt += amount[i]!!.toDouble()

                        val sizeheight = getScreenHeight()
                        val sizewidth = getScreenWidth() * 0.7

                        val face = resources.getFont(R.font.plusjakartatextregular)
                        val boldface = resources.getFont(R.font.plusjakartatexbold)


                        //Create  CardView
                        val Card = context?.let { CardView(it) }
                        Card!!.setLayoutParams(
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                        )
                        Card.radius = 18F
                        Card.setContentPadding(25, 25, 25, 25)
                        Card.setCardBackgroundColor(Color.parseColor("#32cd32"))
                        Card.resources.getDrawable(R.drawable.black_background)
                        Card.cardElevation = 8F
                        Card.maxCardElevation = 12F
                        setMargins(
                            Card,
                            (sizewidth * .1).toInt(),
                            ((i * sizeheight) * 0.3).toInt(),
                            (sizewidth * .1).toInt(),
                            (sizewidth * .1).toInt(),
                        )
                        scrollLayout.addView(Card)


                        //Create Name Text
                        val Name = TextView(context)
                        Name?.textSize = 25f
                        Name?.text = name.get(i).toString()
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


                        //Create Amount Text
                        val Amount = TextView(context)
                        Amount?.text = amount.get(i).toString()
                        Amount.textSize = 35f
                        Amount.setTextColor(Color.WHITE)
                        Amount.setGravity(Gravity.START)
                        Card.addView(Amount)
                        setMargins(
                            Amount,
                            0,
                            (sizeheight * .07).toInt(),
                            (sizewidth * .03).toInt(),
                            0
                        )


                        //Create Label Text
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
                        Label.setTypeface(face)
                        Label.setTranslationZ(18F)
                        setMargins(
                            Label,
                            (sizewidth * .52).toInt(),
                            (sizeheight * .07).toInt(),
                            1,
                            1
                        )
                        Card.addView(Label)

                        //Create Time Text
                        val Time = TextView(context)
                        Time.textSize = 14f
                        Time?.text = time.get(i).toString()
                        Time.setTextColor(Color.WHITE)
                        Time.setLayoutParams(
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                            )
                        )

                        Time.setGravity(Gravity.CENTER)
                        Time.setTypeface(face)
                        Time.setTranslationZ(18F)
                        setMargins(
                            Time,
                            (sizewidth * .07).toInt(),
                            (sizeheight * .165).toInt(),
                            0,
                            0
                        )
                        Card.addView(Time)
                    }

                    }

                } else {
                    Log.d(TAG, "No such document")

                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }


    fun getScreenWidth(): Int {
        val display: Display = requireActivity().windowManager.defaultDisplay
        val screenWidth: Int = display.getWidth()

        return screenWidth
    }

    fun getScreenHeight(): Int {
        val display: Display = requireActivity().windowManager.defaultDisplay
        val screenHeight: Int = display.getHeight()

        return screenHeight
    }


    fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
        if (v.getLayoutParams() is ViewGroup.MarginLayoutParams) {
            val p = v.getLayoutParams() as ViewGroup.MarginLayoutParams
            p.setMargins(l, t, r, b)
            v.requestLayout()
        }

    }

}