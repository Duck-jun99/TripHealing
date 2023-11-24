package com.healingapp.triphealing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.healingapp.triphealing.R
import com.healingapp.triphealing.network.post.ItemRecRV
import com.healingapp.triphealing.network.trip.ItemRegionRV
import com.healingapp.triphealing.secret.Secret

class RegionAdapter(val itemList: ArrayList<ItemRegionRV>) :
    RecyclerView.Adapter<RegionAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_choose_region, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.tvRegion.text = itemList[position].region
        //holder.tv_author.text = itemList[position].author

        Glide.with(holder.imgRegion.context)
            .load(
                Secret.MEDIA_URL+itemList[position].regionImage)
            .into(holder.imgRegion)
        holder.imgRegion.clipToOutline = true

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
        val tvRegion = itemView.findViewById<TextView>(R.id.tvRegion)
        val imgRegion = itemView.findViewById<ImageView>(R.id.imgRegion)


    }

}