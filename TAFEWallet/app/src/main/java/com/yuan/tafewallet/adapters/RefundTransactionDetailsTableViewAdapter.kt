package com.yuan.tafewallet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.WestpacTransaction

class RefundTransactionDetailsTableViewAdapter(var refundedTransaction: WestpacTransaction)
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
                holder.cardDetails.text = refundedTransaction.transactionType
            }
            1 -> {
                holder.cardTitle.text = "Status"
                holder.cardDetails.text = refundedTransaction.status
            }
            2 -> {
                holder.cardTitle.text = "Currency"
                holder.cardDetails.text = refundedTransaction.totalAmount.currency
            }
            3 -> {
                holder.cardTitle.text = "Refund Amount"
                holder.cardDetails.text = "$" + "%.2f".format(refundedTransaction.totalAmount.amount)
            }
            4 -> {
                holder.cardTitle.text = "Receipt Number"
                holder.cardDetails.text = refundedTransaction.receiptNumber
            }
            else -> {
                print("refund transaction details table view index out of range")
            }
        }
        if (position % 2 > 0) {
            holder.itemView.setBackgroundColor(Color.WHITE)
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#efeff4"))
        }
    }
}