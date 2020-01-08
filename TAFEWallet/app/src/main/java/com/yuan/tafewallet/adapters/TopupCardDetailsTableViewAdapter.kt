package com.yuan.tafewallet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.WestpacAccount

class TopupCardDetailsTableViewAdapter(private var westpacAccount: WestpacAccount)
    : RecyclerView.Adapter<TopupCardDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopupCardDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_topup_card_details_table, parent, false)

        return TopupCardDetailsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onBindViewHolder(holder: TopupCardDetailsViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.cardTitle.text = "Card Type"
                holder.cardDetails.text = westpacAccount.cardScheme + " " + westpacAccount.cardType
            }
            1 -> {
                holder.cardTitle.text = "Card Number"
                holder.cardDetails.text = westpacAccount.cardNumber
            }
            2 -> {
                holder.cardTitle.text = "Card Holder Name"
                holder.cardDetails.text = westpacAccount.cardholderName
            }
            3 -> {
                holder.cardTitle.text = "Expiry Date"
                holder.cardDetails.text = westpacAccount.expiryDateMonth + "/20" + westpacAccount.expiryDateYear
            }
            else -> {
                print("topup card details table view index out of range")
            }
        }
        if (position % 2 > 0) {
            holder.itemView.setBackgroundColor(Color.WHITE)
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#efeff4"))
        }
    }
}

class TopupCardDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardTitle = itemView.findViewById(R.id.CardTitle) as TextView
    val cardDetails = itemView.findViewById(R.id.CardDetails) as TextView
}