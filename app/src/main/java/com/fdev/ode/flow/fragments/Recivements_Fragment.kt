package com.fdev.ode.flow.fragments

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.fdev.ode.BaseClass
import com.fdev.ode.flow.main.MainActivity
import com.fdev.ode.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Recivements_Fragment : Fragment() {

    val baseClass = BaseClass()
    lateinit var scrollLayout: RelativeLayout
    private lateinit var blackFilter: ImageView
    private lateinit var areYouSureCard: CardView
    private lateinit var deletebutton: Button
    private lateinit var dontDeletebutton: Button
    private var db = Firebase.firestore
    private var user = Firebase.auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recivement_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scrollLayout = view.findViewById(R.id.receivementsRelativeLayout)
        blackFilter = view.findViewById(R.id.receivementsBlackFilter)
        areYouSureCard = view.findViewById(R.id.receivementsDeleteDebtCard)
        deletebutton = view.findViewById(R.id.receivementsDeleteDebtBtn)
        dontDeletebutton = view.findViewById(R.id.receivementsNotDeleteDebtBtn)

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

                    amount = document?.get("amount") as ArrayList<Long?>
                    id = document?.get("id") as ArrayList<String?>
                    label = document?.get("label") as ArrayList<String?>
                    name = document?.get("name") as ArrayList<String?>
                    mail = document?.get("to") as ArrayList<String?>
                    time = document?.get("time") as ArrayList<String?>

                    for (i in amount.indices) {
                        if (name[i] != null &&
                            label[i] != null &&
                            amount[i] != null &&
                            time[i] != null
                        ) {
                            var j = i + 1
                            val sizeHeight = baseClass.getScreenHeight(requireActivity())
                            val sizeWidth = baseClass.getScreenWidth(requireActivity()) * 0.7
                            val font = resources.getFont(R.font.plusjakartatextregular)
                            val boldFont = resources.getFont(R.font.plusjakartatexbold)

                            //Create  CardView
                            val cardView = context?.let { CardView(it) }
                            cardView!!.setLayoutParams(
                                RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                                )
                            )
                            cardView.radius = 18F
                            cardView.setContentPadding(25, 25, 25, 25)
                            cardView.setCardBackgroundColor(Color.parseColor(("#c71585")))
                            cardView.resources.getDrawable(R.drawable.black_background)
                            cardView.cardElevation = 8F
                            cardView.maxCardElevation = 12F
                            scrollLayout.addView(cardView)
                            baseClass.setMargins(
                                cardView,
                                (sizeWidth * .1).toInt(),
                                ((i * sizeHeight) * 0.35).toInt(),
                                (sizeWidth * .1).toInt(),
                                (sizeWidth * .1).toInt(),
                            )

                            val nameText = TextView(context)
                            nameText.textSize = 25f
                            nameText?.text = name.get(i).toString()
                            nameText.setLayoutParams(
                                RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                )
                            )

                            nameText.setGravity(Gravity.START);
                            nameText.setTypeface(boldFont)
                            nameText.setTranslationZ(35F)
                            nameText.setTextColor(Color.WHITE)
                            baseClass.setMargins(
                                nameText,
                                (sizeWidth * .07).toInt(),
                                (sizeHeight * .01).toInt(),
                                0,
                                0
                            )
                            cardView.addView(nameText)

                            val labelText = TextView(context)
                            labelText.textSize = 16f
                            labelText?.text = label[i].toString()
                            labelText.setTextColor(Color.WHITE)
                            labelText.setLayoutParams(
                                RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                )
                            )
                            labelText.setGravity(Gravity.START);
                            labelText.setTypeface(font)
                            labelText.setTranslationZ(18F)
                            baseClass.setMargins(
                                labelText,
                                (sizeWidth * .07).toInt(),
                                (sizeHeight * .08).toInt(),
                                (sizeWidth * .60).toInt(),
                                10
                            )
                            cardView.addView(labelText)

                            val amountText = TextView(context)
                            amountText?.text = amount[i].toString()
                            amountText.textSize = 50f
                            amountText.setTextColor(Color.WHITE)
                            amountText.setGravity(Gravity.END)
                            cardView.addView(amountText)
                            baseClass.setMargins(
                                amountText,
                                0,
                                (sizeHeight * .05).toInt(),
                                (sizeWidth * .03).toInt(),
                                0
                            )

                            val timeText = TextView(context)
                            timeText.textSize = 14f
                            timeText?.text = time[i].toString()
                            timeText.setTextColor(Color.WHITE)
                            timeText.setLayoutParams(
                                RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                )
                            )
                            timeText.setGravity(Gravity.START)
                            timeText.setTypeface(font)
                            timeText.setTranslationZ(18F)
                            baseClass.setMargins(
                                timeText,
                                (sizeWidth * .07).toInt(),
                                (sizeHeight * .165).toInt(),
                                0,
                                0
                            )
                            cardView.addView(timeText)

                            val deleteText = ImageButton(context)
                            deleteText.setImageResource(R.drawable.white_trash);
                            deleteText.setBackgroundColor(Color.TRANSPARENT)
                            deleteText.setTranslationZ(18F)
                            cardView.addView(deleteText)
                            baseClass.setMargins(
                                deleteText,
                                (sizeWidth * .01).toInt(),
                                (sizeHeight * .15).toInt(),
                                0,
                                0
                            )

                            deleteText.setOnClickListener()
                            {
                                blackFilter.visibility = View.VISIBLE
                                areYouSureCard.visibility = View.VISIBLE

                                dontDeletebutton.setOnClickListener()
                                {
                                    blackFilter.visibility = View.INVISIBLE
                                    areYouSureCard.visibility = View.INVISIBLE
                                }

                                deletebutton.setOnClickListener() {
                                    deleteRecievement(amount, id, label, name, mail, time, i)

                                }
                            }
                        }
                    }
                }
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

        deleteDebt(amount, id, label, name, mail, time, i)

        //delete receivement
        val user = user!!.email.toString()

        amount.removeAt(i)
        id.removeAt(i)
        label.removeAt(i)
        name.removeAt(i)
        mail.removeAt(i)
        time.removeAt(i)

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
        val email = mail.get(i).toString()
        val idIndex = id.get(i).toString()

        var amount = ArrayList<Long?>()
        var id = ArrayList<String?>()
        var label = ArrayList<String?>()
        var name = ArrayList<String?>()
        var mail = ArrayList<String?>()
        var time = ArrayList<String?>()

        //Get the debts
        val docRef = db.collection("Debts").document(email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    amount = document?.get("amount") as ArrayList<Long?>
                    id = document?.get("id") as ArrayList<String?>
                    label = document?.get("label") as ArrayList<String?>
                    name = document?.get("name") as ArrayList<String?>
                    mail = document?.get("to") as ArrayList<String?>
                    time = document?.get("time") as ArrayList<String?>

                    for (j in id.indices) {
                        //Locate  the debt
                        if (id[i]!! == idIndex) {

                            amount.removeAt(j) //delete located amount
                            id.removeAt(j) // delete located id
                            label.removeAt(j) // delete located label
                            name.removeAt(j) // delete located name
                            mail.removeAt(j) // delete located mail
                            time.removeAt(j) // delete located time

                            //Delete Debt
                            delete("Debts", email, "amount", amount)
                            delete("Debts", email, "id", id)
                            delete("Debts", email, "label", label)
                            delete("Debts", email, "name", name)
                            delete("Debts", email, "to", mail)
                            delete("Debts", email, "time", time)

                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(
                                intent,
                                ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
                            )

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
}