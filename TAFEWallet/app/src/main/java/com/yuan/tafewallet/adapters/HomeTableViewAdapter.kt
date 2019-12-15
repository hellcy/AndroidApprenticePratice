package com.yuan.tafewallet.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.PaperCutAccountManager

class HomeTableViewAdapter(context: Context) : RecyclerView.Adapter<HomeTableViewHolder>() {
    val paperCutAccountManager = PaperCutAccountManager(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeTableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_holder_home_table, parent, false)

        return HomeTableViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun onBindViewHolder(holder: HomeTableViewHolder, position: Int) {
        when (position) {
            0 -> {
                holder.cellTitle.text = "Username"
                holder.cellDetail.text = paperCutAccountManager.readPrimaryAccount().UserName
            }
            1 -> {
                holder.cellTitle.text = "Name"
                holder.cellDetail.text = paperCutAccountManager.readPrimaryAccount().FullName
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

class HomeTableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val cellTitle = itemView.findViewById(R.id.homeTableCellTitle) as TextView
    val cellDetail = itemView.findViewById(R.id.homeTableCellDetail) as TextView
}