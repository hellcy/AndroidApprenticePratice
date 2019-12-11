package com.yuan.tafewallet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.WestpacTransaction

class RefundCardDetailsTableViewAdapter(var transaction: WestpacTransaction)
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
                holder.cardDetails.text = transaction.creditCard.cardScheme + " " + transaction.creditCard.cardType
            }
            1 -> {
                holder.cardTitle.text = "Card Number"
                holder.cardDetails.text = transaction.creditCard.cardNumber
            }
            2 -> {
                holder.cardTitle.text = "Card Holder Name"
                holder.cardDetails.text = transaction.creditCard.cardholderName
            }
            3 -> {
                holder.cardTitle.text = "Expiry Date"
                holder.cardDetails.text = transaction.creditCard.expiryDateMonth + "/20" + transaction.creditCard.expiryDateYear
            }
            else -> {
                print("refund card details table view index out of range")
            }
        }
        if (position % 2 > 0) {
            holder.itemView.setBackgroundColor(Color.WHITE)
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#efeff4"))
        }
    }
}