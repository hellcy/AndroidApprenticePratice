package com.yuan.tafewallet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.WestpacAccount

class TopupSelectCardTableViewAdapter(private var westpacAccounts: ArrayList<WestpacAccount>, val clickListener: TopupSelectCardTableViewClickListener)
    : RecyclerView.Adapter<TopupSelectCardViewHolder>() {
    interface TopupSelectCardTableViewClickListener {
        fun listItemClicked(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopupSelectCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_topup_select_card_table, parent, false)

        return TopupSelectCardViewHolder(view)
    }

    override fun getItemCount(): Int {
        return westpacAccounts.size
    }

    override fun onBindViewHolder(holder: TopupSelectCardViewHolder, position: Int) {
        holder.cardNumber.text = westpacAccounts[position].cardNumber
        holder.expiryDate.text = westpacAccounts[position].expiryDateMonth + "/20" + westpacAccounts[position].expiryDateYear
        holder.itemView.setOnClickListener {
            clickListener.listItemClicked(position)
        }
        if (position % 2 > 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#cd8bda"))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#aa7bc9"))
        }
    }
}

class TopupSelectCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cardNumber = itemView.findViewById(R.id.topupCardNumber) as TextView
    val expiryDate = itemView.findViewById(R.id.topupCardExpiry) as TextView
}