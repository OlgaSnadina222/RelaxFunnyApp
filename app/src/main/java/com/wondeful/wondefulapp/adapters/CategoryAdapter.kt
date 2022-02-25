package com.wondeful.wondefulapp.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wondeful.wondefulapp.R
import com.wondeful.wondefulapp.databinding.CategoryItemBinding

class CategoryAdapter(var listener: Listener): ListAdapter<String, CategoryAdapter.Holder> (Comparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = CategoryItemBinding.bind(view)
        fun setData(text: String, listener: Listener) = with(binding){
            tvCatTitle.text = text
            cardViewCat.backgroundTintList = ColorStateList.valueOf(Color.parseColor(ContentManager.colorList[adapterPosition]))
            itemView.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }
    }

    class Comparator : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener{
        fun onClick(position: Int)
    }
}