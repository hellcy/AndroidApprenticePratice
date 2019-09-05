package com.yuan.listmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListSelectionRecyclerViewAdapter(val lists : ArrayList<TaskList>) : RecyclerView.Adapter<ListSelectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {
        // 1 LayoutInflater is a system utility used to instantiates a layout XML file into
        //its corresponding View objects.
        val view = LayoutInflater.from(parent?.context)
            .inflate(R.layout.list_selection_view_holder, parent, false)
        // 2
        return ListSelectionViewHolder(view)
    }

    //  determines how many items the RecyclerView will have. You want the
    //size of your array to be the size of your RecyclerView, so you’ll return that
    override fun getItemCount(): Int {
        return lists.size
    }

    //This method binds the desired data to the ViewHolder at the appropriate position. This
    //will be called repeatedly as you scroll through your RecyclerView.
    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
        if (holder != null) {
            holder.listPosition.text = (position + 1).toString()
            holder.listTitle.text = lists.get(position).name
        }
    }

    fun addList(list: TaskList) {
        // 1
        lists.add(list)
        // 2
        notifyDataSetChanged()
    }
}