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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater!!.inflate(R.layout.fragment_debts_, container, false)
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

        val color = arrayOf("#de495d","#6d32f8","#0c97fa","#00ffc9")

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

                    Log.d(TAG, "amount: ${amount}")

                    Log.d(TAG, amount.size.toString())

                    for (i in 0..amount.size - 1) {
                        val j = i + 1
                        val sizeheight = getScreenHeight()
                        val sizewidth = getScreenWidth() * 0.7

                        val face = resources.getFont(R.font.plusjakartatextregular)
                        val boldface = resources.getFont(R.font.plusjakartatexbold)


                        val randomColor = Random.nextInt(0, 4)
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
                        Card.setCardBackgroundColor(Color.parseColor(color[randomColor])) //Set Background color randomly
                        Card.resources.getDrawable(R.drawable.black_background)
                        Card.cardElevation = 8F
                        Card.maxCardElevation = 12F
                        scrollLayout.addView(Card)
                        setMargins(
                            Card,
                            (sizewidth * .1).toInt(),
                            ((i * sizeheight) * 0.15).toInt(),
                            (sizewidth * .1).toInt(),
                            100
                        )


                        //Create Name Text
                        val Contact_ = TextView(context)
                        Contact_?.textSize = 20f
                        Contact_?.text = name.get(i)
                        Contact_.setLayoutParams(
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                            )
                        )

                        Contact_.setGravity(Gravity.CENTER);
                        Contact_.setTypeface(boldface)
                        Contact_.setTranslationZ(20F)
                        Contact_.setTextColor(Color.WHITE)
                        Card.addView(Contact_)


                        //Create Amount Text
                        var Amount = TextView(context)
                        Amount.text = amount.get(i).toString()
                        Amount.textSize = 55f
                        Amount.setTextColor(Color.WHITE)
                        Amount.setGravity(Gravity.START)
                        Amount.setLayoutParams(
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                            )
                        )
                        setMargins(Amount, 1, ((Card.height) *.2).toInt(), (sizewidth * .8).toInt(), 1)
                        Card.addView(Amount)


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
                        Label.setPadding((sizewidth * .3).toInt(),0,0,0)
                        setMargins(Label,(sizewidth *.1).toInt(),(sizeheight * .04).toInt(),1,1)
                        Card.addView(Label)


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