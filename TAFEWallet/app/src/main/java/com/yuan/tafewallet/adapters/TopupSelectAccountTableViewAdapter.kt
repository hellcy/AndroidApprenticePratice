package com.yuan.tafewallet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.PaperCutAccount

class TopupSelectAccountTableViewAdapter(private val paperCutAccounts: ArrayList<PaperCutAccount>, private val clickListener: TopupSelectAccountTableViewClickListener)
    : RecyclerView.Adapter<TopupSelectAccountViewHolder>() {

    interface TopupSelectAccountTableViewClickListener {
        fun listItemClicked(account: PaperCutAccount)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopupSelectAccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_topup_select_account_table, parent, false)

        return TopupSelectAccountViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: TopupSelectAccountViewHolder, position: Int) {
        val paperCutAccount = paperCutAccounts[0]
        when (position) {
            0 -> {
                holder.accountName.text = paperCutAccount.AccountName
                holder.accountBalance.text = convertDollarSign(paperCutAccount.Balance)
                holder.itemView.setOnClickListener {
                    clickListener.listItemClicked(paperCutAccount)
                }
            }
            else -> {
                print("home table view index out of range")
            }
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

class TopupSelectAccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val accountName = itemView.findViewById(R.id.topupAccountName) as TextView
    val accountBalance = itemView.findViewById(R.id.topupAccountBalance) as TextView
}