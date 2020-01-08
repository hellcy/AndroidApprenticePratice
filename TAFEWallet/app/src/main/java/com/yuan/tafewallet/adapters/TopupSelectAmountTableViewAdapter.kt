package com.yuan.tafewallet.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R

class TopupSelectAmountTableViewAdapter(private val clickListener: TopupSelectAmountTableViewClickListener)
    : RecyclerView.Adapter<TopupSelectAmountViewHolder>() {

    private val amountInts: List<Int> = listOf(5, 10, 15, 20, 50)
    private val amounts: List<String> = listOf("$5.00", "$10.00", "$15.00", "$20.00", "$50.00")

    interface TopupSelectAmountTableViewClickListener {
        fun listItemClicked(amount: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopupSelectAmountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_topup_select_amount_table, parent, false)

        return TopupSelectAmountViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: TopupSelectAmountViewHolder, position: Int) {
        holder.topupAmount.text = amounts[position]
        holder.itemView.setOnClickListener {
            clickListener.listItemClicked(amountInts[position])
        }

        if (position % 2 > 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#cd8bda"))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#aa7bc9"))
        }
    }
}

class TopupSelectAmountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val topupAmount = itemView.findViewById(R.id.topupAmount) as TextView

}