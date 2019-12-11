package com.yuan.tafewallet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.WestpacTransaction

class RefundConfirmTableViewAdapter(val transactions: ArrayList<WestpacTransaction>, val clickListener: RefundConfirmTableViewClickListener)
    : RecyclerView.Adapter<RefundConfirmViewHolder>() {

    interface RefundConfirmTableViewClickListener {
        fun listItemClicked(position: Int)
    }

    // reuse top up select account view holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RefundConfirmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_refund_confirm_table, parent, false)

        return RefundConfirmViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: RefundConfirmViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.cardNumber.text = transaction.creditCard.cardNumber
        holder.expiryDate.text = transaction.creditCard.expiryDateMonth + "/20" + transaction.creditCard.expiryDateYear
        holder.refundAmount.text = "Refund: $" + transaction.refundableAmount

        holder.itemView.setOnClickListener {
            clickListener.listItemClicked(position)
        }

        if (position % 2 > 0) {
            holder.itemView.setBackgroundColor(Color.WHITE)
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#efeff4"))
        }
    }
}

class RefundConfirmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardNumber = itemView.findViewById(R.id.cardNumber) as TextView
    val expiryDate = itemView.findViewById(R.id.expiryDate) as TextView
    val refundAmount = itemView.findViewById(R.id.refundAmount) as TextView
}