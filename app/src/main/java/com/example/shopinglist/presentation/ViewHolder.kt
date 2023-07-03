package com.example.shopinglist.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shopinglist.R

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvName = itemView.findViewById<TextView>(R.id.textViewName)
    var tvCount = itemView.findViewById<TextView>(R.id.textViewCount)
}
