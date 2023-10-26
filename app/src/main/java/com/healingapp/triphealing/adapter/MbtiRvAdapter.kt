package com.healingapp.triphealing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.healingapp.triphealing.R
import com.healingapp.triphealing.network.post.ItemFamRV
import com.healingapp.triphealing.network.post.ItemMbtiRV
import com.healingapp.triphealing.network.post.ItemRecRV
import com.healingapp.triphealing.secret.Secret

class MbtiRvAdapter(val itemList: ArrayList<ItemMbtiRV>) :
    RecyclerView.Adapter<MbtiRvAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mbti_post, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.tv_title.text = itemList[position].title
        holder.tv_author.text = itemList[position].author
        holder.tv_text.text = itemList[position].text
        Glide.with(holder.img.context)
            .load(
            Secret.MEDIA_URL+itemList[position].coverImage)
            .into(holder.img)
        holder.img.clipToOutline = true

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener


    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_title = itemView.findViewById<TextView>(R.id.tv_mbti_title)
        val tv_author = itemView.findViewById<TextView>(R.id.tv_mbti_author)
        val img = itemView.findViewById<ImageView>(R.id.img_mbti_rv)
        val tv_text = itemView.findViewById<TextView>(R.id.tv_mbti_introduce)


    }

}