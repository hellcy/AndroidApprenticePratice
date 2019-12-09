package com.yuan.tafewallet.ui.topup

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R

class TopupSelectAccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val accountName = itemView?.findViewById(R.id.topupAccountName) as TextView
    val accountBalance = itemView?.findViewById(R.id.topupAccountBalance) as TextView
}