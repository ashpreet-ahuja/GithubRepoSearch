package com.ashpreet.demo.githubuserrepo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ashpreet.demo.githubuserrepo.R

class PastSearchAdapter(
    private val pastSearches: List<String>,
    private val onItemClick: (String) -> Unit  // Lambda for handling clicks
) : RecyclerView.Adapter<PastSearchAdapter.PastSearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PastSearchViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_past_search, parent, false)
        return PastSearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: PastSearchViewHolder, position: Int) {
        val searchText = pastSearches[position]
        holder.bind(searchText, onItemClick)
    }

    override fun getItemCount(): Int = pastSearches.size

    class PastSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val searchText: TextView = itemView.findViewById(R.id.tvSearchText)

        fun bind(text: String, onItemClick: (String) -> Unit) {
            searchText.text = text
            itemView.setOnClickListener { onItemClick(text) }
        }
    }
}