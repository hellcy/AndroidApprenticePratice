package com.yuan.tafewallet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.WestpacTransaction

class RefundCompleteTableViewAdapter(private val transactions: ArrayList<WestpacTransaction>, val clickListener: RefundCompleteTableViewClickListener)
    : RecyclerView.Adapter<RefundCompleteViewHolder>() {

    interface RefundCompleteTableViewClickListener {
        fun listItemClicked(position: Int)
    }

    // reuse top up select account view holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RefundCompleteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_refund_complete_table, parent, false)

        return RefundCompleteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: RefundCompleteViewHolder, position: Int) {
        holder.transactionDetails.text = "Transaction Details (" + (position + 1) + " of " + transactions.size + ")"

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

class RefundCompleteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val transactionDetails = itemView.findViewById(R.id.transactionDetails) as TextView
}