package com.example.newsroom

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.newsroom.data.NewsData

import com.example.newsroom.placeholder.PlaceholderContent.PlaceholderItem
import com.example.newsroom.databinding.NewsListItemBinding
import com.example.newsroom.placeholder.PlaceholderContent

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class NewsRecyclerViewAdapter(
    private var values: List<NewsData> = ArrayList()
) : RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>() {


    private var listener: OnClickListener? = null

    // A function to bind the onclickListener.
    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: NewsData)
        fun onLongClick(position: Int, model: NewsData)
        fun onClickToEdit(position: Int, model: NewsData)
        fun onClickToDelete(position: Int, model: NewsData)
    }

    fun submitList(d: List<NewsData>) {

//        values.clear()
//        values.addAll(d)
        values = d
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            NewsListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        val cpi = PlaceholderContent.createPlaceholderItem(position, item)
        holder.idView.text = cpi.id
        holder.contentView.text = cpi.content
        holder.btnEditView.setOnClickListener({
            if (listener != null) {
                listener!!.onClickToEdit(position, item )
            }
        })
        holder.btnDeleteView.setOnClickListener({
            if (listener != null) {
                listener!!.onClickToDelete(position, item )
            }
        })
        holder.itemView.setOnClickListener {
            if (listener != null) {
                listener!!.onClick(position, item )
            }
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: NewsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        val btnEditView: TextView = binding.buttonEdit
        val btnDeleteView: TextView = binding.buttonDelete


        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}