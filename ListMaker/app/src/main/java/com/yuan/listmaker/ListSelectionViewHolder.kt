package com.yuan.listmaker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ListSelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val listPosition = itemView.findViewById<TextView>(R.id.itemNumber) as TextView
    val listTitle = itemView.findViewById<TextView>(R.id.itemString) as TextView
}