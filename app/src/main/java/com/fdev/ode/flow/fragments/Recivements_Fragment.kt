package com.fdev.ode.flow.fragments

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_recivement_.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Recivements_Fragment : Fragment() {

    private val baseClass = BaseClass()
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

        val docRef = db.collection("Recivements").document(user?.email.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    val amount = document?.get("amount") as ArrayList<Long?>
                    val id = document.get("id") as ArrayList<String?>
                    val label = document.get("label") as ArrayList<String?>
                    val name = document.get("name") as ArrayList<String?>
                    val mail = document.get("to") as ArrayList<String?>
                    val time = document.get("time") as ArrayList<String?>

                    for (i in amount.indices) {
                        if (name[i] != null &&
                            label[i] != null &&
                            amount[i] != null &&
                            time[i] != null
                        ) {
                            val sizeHeight = baseClass.getScreenHeight(requireActivity())
                            val sizeWidth = baseClass.getScreenWidth(requireActivity()) * 0.7
                            val font = resources.getFont(R.font.plusjakartatextregular)
                            val boldFont = resources.getFont(R.font.plusjakartatexbold)

                            val cardView = context?.let { CardView(it) }
                            cardView!!.layoutParams = RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                            cardView.radius = 18F
                            cardView.setContentPadding(25, 25, 25, 25)
                            cardView.setCardBackgroundColor(Color.parseColor(("#c71585")))
                            cardView.resources.getDrawable(R.drawable.black_background)
                            cardView.cardElevation = 8F
                            cardView.maxCardElevation = 12F
                            receivementsRelativeLayout.addView(cardView)
                            baseClass.setMargins(
                                cardView,
                                (sizeWidth * .1).toInt(),
                                ((i * sizeHeight) * 0.4).toInt(),
                                (sizeWidth * .1).toInt(),
                                0,
                            )

                            val nameText = TextView(context)
                            baseClass.setNameView(nameText, name[i].toString(),sizeWidth,boldFont)
                            cardView.addView(nameText)

                            val timeText = TextView(context)
                            baseClass.setDateView(timeText,time[i].toString(), font)
                            cardView.addView(timeText)

                            val labelText = TextView(context)
                            baseClass.setLabelView(labelText, label[i].toString(), (sizeHeight * .1).toInt(), font)
                            cardView.addView(labelText)

                            val amountText = TextView(context)
                            baseClass.setAmountView(amountText, amount[i].toString(),(sizeHeight * .2).toInt(),boldFont)
                            cardView.addView(amountText)

                            val deleteText = ImageButton(context)
                            deleteText.setImageResource(R.drawable.white_trash);
                            deleteText.setBackgroundColor(Color.TRANSPARENT)
                            deleteText.translationZ = 18F
                            cardView.addView(deleteText)
                            baseClass.setMargins(
                                deleteText,
                                0,
                                (sizeHeight * .28).toInt(),
                                0, 0
                            )

                            deleteText.setOnClickListener()
                            {
                                receivementsBlackFilter.visibility = View.VISIBLE
                                receivementsDeleteDebtCard.visibility = View.VISIBLE

                                receivementsNotDeleteDebtBtn.setOnClickListener()
                                {
                                    receivementsBlackFilter.visibility = View.INVISIBLE
                                    receivementsDeleteDebtCard.visibility = View.INVISIBLE
                                }

                                receivementsDeleteDebtBtn.setOnClickListener() {
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

        deleteDebt(id, mail, i)

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
        idList: ArrayList<String?>,
        mailList: ArrayList<String?>,
        i: Int
    ) {
        val email = mailList[i].toString()
        val idIndex = idList[i].toString()

        //Get the debts
        val docRef = db.collection("Debts").document(email)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.data != null) {

                    runBlocking {
                        launch {
                            delay(1000)
                            val intent = Intent(context, MainActivity::class.java)
                            startActivity(
                                intent,
                                ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
                            )
                        }

                        val amount = document?.get("amount") as ArrayList<Long?>
                        val id = document.get("id") as ArrayList<String?>
                        val label = document.get("label") as ArrayList<String?>
                        val name = document.get("name") as ArrayList<String?>
                        val mail = document.get("to") as ArrayList<String?>
                        val time = document.get("time") as ArrayList<String?>

                        for (j in id.indices) {
                            //Locate  the debt
                            if (id[i]!! == idIndex) {

                                amount.removeAt(j)
                                id.removeAt(j)
                                label.removeAt(j)
                                name.removeAt(j)
                                mail.removeAt(j)
                                time.removeAt(j)

                                //Delete Debt
                                delete("Debts", email, "amount", amount)
                                delete("Debts", email, "id", id)
                                delete("Debts", email, "label", label)
                                delete("Debts", email, "name", name)
                                delete("Debts", email, "to", mail)
                                delete("Debts", email, "time", time)

                            }
                        }
                    }

                }
            }
    }

    private fun delete(Collection: String, Document: String, Field: String, Array: ArrayList<*>) {
        db.collection(Collection)
            .document(Document)
            .update(Field, Array)
    }
}