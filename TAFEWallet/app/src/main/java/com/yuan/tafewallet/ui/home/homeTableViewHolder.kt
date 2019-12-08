package com.yuan.tafewallet.ui.home

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R

class homeTableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val cellTitle = itemView?.findViewById(R.id.homeTableCellTitle) as TextView
    val cellDetail = itemView?.findViewById(R.id.homeTableCellDetail) as TextView
}