package com.fdev.ode.flow.main.fragments.Receivements

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fdev.ode.util.BaseClass
import com.fdev.ode.R
import com.fdev.ode.util.Views
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ReceivementsViewModel : ViewModel() {

    private val baseClass = BaseClass()
    private val views = Views()
    private var db = Firebase.firestore
    private var user = Firebase.auth.currentUser
    val refresh: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun loadReceivements(
        activity: Activity,
        receivementsRelativeLayout: RelativeLayout,
        receivementsBlackFilter: ImageView,
        receivementsDeleteDebtCard: CardView,
        receivementsNotDeleteDebtBtn: Button,
        receivementsDeleteDebtBtn: Button
    ) {

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
                            val sizeHeight = baseClass.getScreenHeight(activity)
                            val sizeWidth = baseClass.getScreenWidth(activity) * 0.7
                            val font = activity.resources.getFont(R.font.plusjakartatextregular)
                            val boldFont = activity.resources.getFont(R.font.plusjakartatexbold)

                            val cardView = activity.baseContext?.let { CardView(it) }
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

                            val nameText = TextView(activity.baseContext)
                            views.setNameView(nameText, name[i].toString(), sizeWidth, boldFont)
                            cardView.addView(nameText)

                            val timeText = TextView(activity.baseContext)
                            views.setDateView(timeText, time[i].toString(), font)
                            cardView.addView(timeText)

                            val labelText = TextView(activity.baseContext)
                            views.setLabelView(
                                labelText,
                                label[i].toString(),
                                (sizeHeight * .1).toInt(),
                                font
                            )
                            cardView.addView(labelText)

                            val amountText = TextView(activity.baseContext)
                            views.setAmountView(
                                amountText,
                                amount[i].toString(),
                                (sizeHeight * .2).toInt(),
                                boldFont
                            )
                            cardView.addView(amountText)

                            val deleteText = ImageButton(activity.baseContext)
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
        val user = user!!.email.toString()
        //Delete Recievemnets
        delete("Recivements", user, "amount", amount, i)
        delete("Recivements", user, "id", id, i)
        delete("Recivements", user, "label", label, i)
        delete("Recivements", user, "name", name, i)
        delete("Recivements", user, "to", mail, i)
        delete("Recivements", user, "time", time, i)
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
                            refresh.value = true
                        }
                        val amount = document?.get("amount") as ArrayList<Long?>
                        val id = document.get("id") as ArrayList<String?>
                        val label = document.get("label") as ArrayList<String?>
                        val name = document.get("name") as ArrayList<String?>
                        val mail = document.get("to") as ArrayList<String?>
                        val time = document.get("time") as ArrayList<String?>

                        for (j in id.indices) {
                            if (id[i]!! == idIndex) {
                                delete("Debts", email, "amount", amount, j)
                                delete("Debts", email, "id", id, j)
                                delete("Debts", email, "label", label, j)
                                delete("Debts", email, "name", name, j)
                                delete("Debts", email, "to", mail, j)
                                delete("Debts", email, "time", time, j)
                            }
                        }
                    }

                }
            }
    }

    private fun delete(
        Collection: String,
        Document: String,
        Field: String,
        Array: ArrayList<*>,
        index: Int
    ) {
        Array.removeAt(index)
        db.collection(Collection)
            .document(Document)
            .update(Field, Array)
    }
}