package com.yuan.tafewallet.ui.topup

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.Account


class TopupSelectAccountTableViewAdapter(val clickListener: TopupSelectAccountTableViewClickListener)
    : RecyclerView.Adapter<TopupSelectAccountViewHolder>() {

    private var account = Account("Primary account", "$8.95")

    interface TopupSelectAccountTableViewClickListener {
        fun listItemClicked(account: Account)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TopupSelectAccountViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.topup_select_account_table_view_holder, parent, false)

        return TopupSelectAccountViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: TopupSelectAccountViewHolder, position: Int) {
        if (holder != null) {
            when (position) {
                0 -> {
                    holder.accountName.text = account.accountName
                    holder.accountBalance.text = account.accountBalance
                    holder.itemView.setOnClickListener {
                        clickListener.listItemClicked(account)
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
    }

}