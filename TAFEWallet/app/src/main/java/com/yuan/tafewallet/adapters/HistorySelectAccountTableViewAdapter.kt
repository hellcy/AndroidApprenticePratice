package com.yuan.tafewallet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.PaperCutAccount

class HistorySelectAccountTableViewAdapter(private val accounts: ArrayList<PaperCutAccount>, private val clickListener: HistorySelectAccountTableViewClickListener)
    : RecyclerView.Adapter<TopupSelectAccountViewHolder>() {

    interface HistorySelectAccountTableViewClickListener {
        fun listItemClicked(position: Int)
    }

    // reuse top up select account view holder
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopupSelectAccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_topup_select_account_table, parent, false)

        return TopupSelectAccountViewHolder(view)
    }

    override fun getItemCount(): Int {
        return accounts.size
    }

    override fun onBindViewHolder(holder: TopupSelectAccountViewHolder, position: Int) {
        val account = accounts[position]
        holder.accountName.text = account.AccountName
        holder.accountBalance.text = convertDollarSign(account.Balance)
        holder.itemView.setOnClickListener {
            clickListener.listItemClicked(position)
        }

        if (position % 2 > 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#cd8bda"))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#aa7bc9"))
        }
    }

    fun convertDollarSign(balance: Double): String {
        if (balance >= 0) {
            return "$" + "%.2f".format(balance)
        } else {
            return "-$" + "%.2f".format(-balance)
        }
    }
}