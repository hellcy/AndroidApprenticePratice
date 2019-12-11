package com.yuan.tafewallet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.Transaction

class HistoryTransactionsTableViewAdapter(val transactions: ArrayList<Transaction>, val clickListener: HistoryTransactionsTableViewClickListener)
    : RecyclerView.Adapter<HistoryTransactionsViewHolder>() {

    interface HistoryTransactionsTableViewClickListener {
        fun listItemClicked(position: Int)
    }

    // reuse top up select account view holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryTransactionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_history_transactions_table, parent, false)

        return HistoryTransactionsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: HistoryTransactionsViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.transactionType.text = transaction.Comments!!.split("-")[0]
        holder.transactionTime.text = transaction.TransactionDate
        holder.transactionAmount.text = "$" + "%.2f".format(transaction.amount)
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


class HistoryTransactionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val transactionType = itemView.findViewById(R.id.TransactionType) as TextView
    val transactionTime = itemView.findViewById(R.id.TransactionTime) as TextView
    val transactionAmount = itemView.findViewById(R.id.TransactionAmount) as TextView
}