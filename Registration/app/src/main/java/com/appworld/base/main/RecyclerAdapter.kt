package com.appworld.base.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.appworld.base.R


class RecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var userNames = arrayOf("Alex", "Ben", "Catherine", "David", "Evan", "Farran", "George", "Helan")

    private val LIST_ITEM = 0
    private val GRID_ITEM = 1

    var isSwitchView = true

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textViewName: TextView = view.findViewById(R.id.textViewName)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isSwitchView){
            LIST_ITEM
        }else{
            GRID_ITEM
        }
    }

    fun toggleItemViewType(): Boolean {
        isSwitchView = !isSwitchView
        return isSwitchView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View = if (viewType === LIST_ITEM) {
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_view, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                    .inflate(R.layout.grid_item_view, parent, false)
        }
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userNames.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ItemViewHolder).textViewName.text = userNames[position]
    }
}