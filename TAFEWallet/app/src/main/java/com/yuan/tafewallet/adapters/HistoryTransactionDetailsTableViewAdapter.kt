package com.yuan.tafewallet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.Transaction

class HistoryTransactionDetailsTableViewAdapter(var transaction: Transaction)
    : RecyclerView.Adapter<TopupCardDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopupCardDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_topup_card_details_table, parent, false)

        return TopupCardDetailsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: TopupCardDetailsViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.cardTitle.text = "Transaction Type"
                holder.cardDetails.text = transaction.TransactionType
            }
            1 -> {
                holder.cardTitle.text = "Description"
                holder.cardDetails.text = transaction.Comments!!.split("-")[0]
            }
            2 -> {
                holder.cardTitle.text = "Amount"
                holder.cardDetails.text = "$" + "%.2f".format(transaction.amount)
            }
            3 -> {
                holder.cardTitle.text = "Reference Number"
                holder.cardDetails.text = transaction.Comments!!.split("-")[1]
            }
            4 -> {
                holder.cardTitle.text = "Date Time"
                holder.cardDetails.text = transaction.TransactionDate
            }
            else -> {
                print("history transaction details table view index out of range")
            }
        }
        if (position % 2 > 0) {
            holder.itemView.setBackgroundColor(Color.WHITE)
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#efeff4"))
        }
    }
}