package com.yuan.listmaker

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class ListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val taskTextView = itemView?.findViewById<TextView>(R.id.textview_task) as TextView
}