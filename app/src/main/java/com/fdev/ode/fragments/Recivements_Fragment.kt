package com.fdev.ode.fragments

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.fdev.ode.MainActivity
import com.fdev.ode.R
import com.fdev.ode.util.DebtController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class Recivements_Fragment : Fragment() {

    lateinit var scrollLayout: RelativeLayout

    private var BlackFilter: ImageView? = null
    private var AreYouSureCard: CardView? = null
    private var Deletebutton: Button? = null
    private var DontDeletebutton: Button? = null
    private val db = Firebase.firestore
    private val user = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recivement_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollLayout = view.findViewById(R.id.Scroll_RelativeofRecivements)
        BlackFilter = view.findViewById(R.id.blackfilterinRecivements)
        AreYouSureCard = view.findViewById(R.id.deleteDebtCardinRecivements)
        Deletebutton = view.findViewById(R.id.deleteDebtBtninRecivements)
        DontDeletebutton = view.findViewById(R.id.notDeleteDebtBtninRecivements)


        val TAG = "LoadRecievements"


        var amount = ArrayList<Long?>()
        var id = ArrayList<String?>()
        var label = ArrayList<String?>()
        var name = ArrayList<String?>()
        var mail = ArrayList<String?>()
        var time = ArrayList<String?>()

        val docRef = db.collection("Recivements").document(user?.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    Log.d(
                        TAG,
                        "DocumentSnapshot data: ${document.get("amount") as ArrayList<String?>}"
                    )
                    amount = document?.get("amount") as ArrayList<Long?>
                    id = document?.get("id") as ArrayList<String?>
                    label = document?.get("label") as ArrayList<String?>
                    name = document?.get("name") as ArrayList<String?>
                    mail = document?.get("to") as ArrayList<String?>
                    time = document?.get("time") as ArrayList<String?>

                    Log.d(TAG, "amount: ${amount}")

                    Log.d(TAG, amount.size.toString())

                    for (i in 0..amount.size - 1) {
                        var j = i + 1
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
                        Card.setCardBackgroundColor(Color.parseColor(("#c71585")))
                        Card.resources.getDrawable(R.drawable.black_background)
                        Card.cardElevation = 8F
                        Card.maxCardElevation = 12F
                        scrollLayout.addView(Card)
                        setMargins(
                            Card,
                            (sizewidth * .1).toInt(),
                            ((i * sizeheight) * 0.25).toInt(),
                            (sizewidth * .1).toInt(),
                            ((i * sizeheight) * 0.25).toInt(),
                        )


                        //Create Name Text
                        val Name = TextView(context)
                        Name.textSize = 25f
                        Name?.text = name.get(i)
                        Name.setLayoutParams(
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                            )
                        )

                        Name.setGravity(Gravity.START);
                        Name.setTypeface(boldface)
                        Name.setTranslationZ(35F)
                        Name.setTextColor(Color.WHITE)
                        setMargins( Name,(sizewidth * .07).toInt(),(sizeheight *.01).toInt(),0,0)
                        Card.addView(Name)


                        //Create Label Text
                        val Label = TextView(context)
                        Label.textSize = 16f
                        Label?.text = label.get(i)
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
                            (sizewidth * .07).toInt(),
                            (sizeheight * .08).toInt(),
                            (sizewidth * .60).toInt(),
                            10
                        )
                        Card.addView(Label)

                        //Create Amount Text
                        val Amount = TextView(context)
                        Amount?.text = amount.get(i).toString()
                        Amount.textSize = 50f
                        Amount.setTextColor(Color.WHITE)
                        Amount.setGravity(Gravity.END)
                        Card.addView(Amount)
                        setMargins(Amount,0,(sizeheight * .05).toInt(),(sizewidth * .03).toInt(),0)


                        //Create Time Text
                        val Time = TextView(context)
                        Time.textSize = 14f
                        Time?.text = time.get(i)
                        Time.setTextColor(Color.WHITE)
                        Time.setLayoutParams(
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                            )
                        )
                        Time.setGravity(Gravity.START)
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

                        val Delete = ImageButton(context)
                        Delete.setImageResource(R.drawable.white_trash);
                        Delete.setBackgroundColor(Color.TRANSPARENT)
                        Delete.setTranslationZ(18F)
                        Card.addView(Delete)
                        setMargins(
                            Delete,
                            (sizewidth * .01).toInt(),
                            (sizeheight * .15).toInt(),
                            0,
                            0
                        )


                        // Confromation Buttons(Yes,No) must be inside of Delete(CardView) in order to get the correct index
                        Delete.setOnClickListener()
                        {
                            BlackFilter?.visibility = View.VISIBLE
                            AreYouSureCard?.visibility = View.VISIBLE

                            //Don't Delete
                            DontDeletebutton?.setOnClickListener()
                            {
                                BlackFilter?.visibility = View.INVISIBLE
                                AreYouSureCard?.visibility = View.INVISIBLE
                            }
                            //Delete Debt
                            Deletebutton?.setOnClickListener() {
                                deleteRecievement(amount, id, label, name, mail,time, i)



                            }
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

    private fun deleteRecievement(
        amount: ArrayList<Long?>,
        id: ArrayList<String?>,
        label: ArrayList<String?>,
        name: ArrayList<String?>,
        mail: ArrayList<String?>,
        time: ArrayList<String?>,
        i: Int
    ) {


        //delete Debt
        deleteDebt(amount, id, label, name, mail,time, i)   // since i is in loop and firebase is working asynchronous it if mandatory to call the delete function here in order to use the same date

        //delete recivedement
        val user = user!!.email.toString()

        var debtController = DebtController()
        debtController.SubstractTotalDebt(amount.get(i)!!.toLong(), user, "to-collect")

        amount.removeAt(i) //delete current amount
        id.removeAt(i) // delete current id
        label.removeAt(i) // delete current label
        name.removeAt(i) // delete current name
        mail.removeAt(i) // delete current mail
        time.removeAt(i) // delete current time

        //Delete Recievemnets
        delete("Recivements", user, "amount", amount)
        delete("Recivements", user, "id", id)
        delete("Recivements", user, "label", label)
        delete("Recivements", user, "name", name)
        delete("Recivements", user, "to", mail)
        delete("Recivements", user, "time", time)


    }

    private fun deleteDebt(
        amount: ArrayList<Long?>,
        id: ArrayList<String?>,
        label: ArrayList<String?>,
        name: ArrayList<String?>,
        mail: ArrayList<String?>,
        time: ArrayList<String?>,
        i: Int
    ) {


        val EMAIL = mail.get(i).toString()
        val ID = id.get(i).toString()

        val TAG = "DeleteDebt"

        Log.d( TAG,"Local Email: ${EMAIL}" )
        Log.d( TAG,"Local Id: ${ID}" )

        var amount = ArrayList<Long?>()
        var id = ArrayList<String?>()
        var label = ArrayList<String?>()
        var name = ArrayList<String?>()
        var mail = ArrayList<String?>()
        var time = ArrayList<String?>()

        //Get the debts
        val docRef = db.collection("Debts").document(EMAIL)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    Log.d(
                        TAG,
                        "DocumentSnapshot data: ${document.get("id") as ArrayList<String?>}"
                    )
                    amount = document?.get("amount") as ArrayList<Long?>
                    id = document?.get("id") as ArrayList<String?>
                    label = document?.get("label") as ArrayList<String?>
                    name = document?.get("name") as ArrayList<String?>
                    mail = document?.get("to") as ArrayList<String?>
                    time = document?.get("time") as ArrayList<String?>

                    for (i in 0..id.size -1)
                    {
                        //Locate  the debt
                        if(id.get(i)!!.equals(ID))
                        {
                            Log.d( TAG,"We Got it Boss: ${id.get(i)}" )
                            Log.d( TAG,"Here's the location: $i" )

                            val debtController = DebtController()
                            debtController.SubstractTotalDebt(amount.get(i)!!.toLong(), EMAIL, "debt")

                            amount.removeAt(i) //delete located amount
                            id.removeAt(i) // delete located id
                            label.removeAt(i) // delete located label
                            name.removeAt(i) // delete located name
                            mail.removeAt(i) // delete located mail
                            time.removeAt(i) // delete located time


                            Log.d( TAG,"amount After remove: $amount" )
                            Log.d( TAG,"id After remove: $id" )
                            Log.d( TAG,"label After remove: $label" )
                            Log.d( TAG,"name After remove: $name" )
                            Log.d( TAG,"mail After remove: $mail" )
                            Log.d( TAG,"time After remove: $time" )

//                            //Delete Debt
                            delete("Debts", EMAIL, "amount", amount)
                            delete("Debts", EMAIL, "id", id)
                            delete("Debts", EMAIL, "label", label)
                            delete("Debts", EMAIL, "name", name)
                            delete("Debts", EMAIL, "to", mail)
                            delete("Debts", EMAIL, "time", time)


                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())

                        }
                    }


                }
            }



    }

    fun delete(Collection: String, Document: String, Field: String, Array: ArrayList<*>) {
        db.collection(Collection)
            .document(Document)
            .update(Field, Array)
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