package com.yuan.tafewallet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.WestpacTransaction

class TopupTransactionCompleteTableViewAdapter(var westpacTransaction: WestpacTransaction)
    : RecyclerView.Adapter<TopupCardDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopupCardDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_topup_card_details_table, parent, false)

        return TopupCardDetailsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 6
    }

    override fun onBindViewHolder(holder: TopupCardDetailsViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.cardTitle.text = "Transaction Type"
                holder.cardDetails.text = westpacTransaction.transactionType
            }
            1 -> {
                holder.cardTitle.text = "Status"
                holder.cardDetails.text = westpacTransaction.status
            }
            2 -> {
                holder.cardTitle.text = "Currency"
                holder.cardDetails.text = westpacTransaction.totalAmount.currency
            }
            3 -> {
                holder.cardTitle.text = "Top Up Amount"
                holder.cardDetails.text = westpacTransaction.totalAmount.amount.toString()
            }
            4 -> {
                holder.cardTitle.text = "Receipt Number"
                holder.cardDetails.text = westpacTransaction.receiptNumber
            }
            5 -> {
                holder.cardTitle.text = "Paid by"
                holder.cardDetails.text = westpacTransaction.creditCard.cardScheme + " " + westpacTransaction.creditCard.cardType + " " + westpacTransaction.creditCard.cardNumber
            }
            else -> {
                print("topup transaction complete table view index out of range")
            }
        }
        if (position % 2 > 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#deb3e7"))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#efeff4"))
        }
    }
}