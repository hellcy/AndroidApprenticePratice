package com.yuan.tafewallet.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R

class HomeTableViewAdapter : RecyclerView.Adapter<HomeTableViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTableViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.home_table_view_holder, parent, false)

        return HomeTableViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: HomeTableViewHolder, position: Int) {
        if (holder != null) {
            when (position) {
                0 -> {
                    holder.cellTitle.text = "Username"
                    holder.cellDetail.text = "a8000761"
                }
                1 -> {
                    holder.cellTitle.text = "Name"
                    holder.cellDetail.text = "Yuan Cheng"
                }
                else -> {
                    print("home table view index out of range")
                }
            }

            if (position % 2 > 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#FAFAFA"))
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#ececec"))
            }
        }
    }
}