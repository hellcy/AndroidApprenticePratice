package com.yuan.listmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListSelectionRecyclerViewAdapter(val lists: ArrayList<TaskList>, val clickListener: ListSelectionRecyclerViewClickListener)
    : RecyclerView.Adapter<ListSelectionViewHolder>() {

    interface ListSelectionRecyclerViewClickListener {
        fun listItemClicked(list: TaskList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {
        // LayoutInflater is a system utility used to instantiates a layout XML file into
        //its corresponding View objects.
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_selection_view_holder, parent, false)
        return ListSelectionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lists.size

    }

    // cellForRowAt in Swift
    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
        if (holder != null) {
            holder.listPosition.text = (position + 1).toString()
            holder.listTitle.text = lists[position].name
            holder.itemView.setOnClickListener {
                clickListener.listItemClicked(lists[position])
            }
        }
    }

    fun addList(list: TaskList) {
        lists.add(list)

        // table.reloadData
        notifyDataSetChanged()  //inform the adapter that it should query its underlying data and update the RecyclerView
    }
}