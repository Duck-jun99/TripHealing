package com.healingapp.triphealing.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.healingapp.triphealing.R
import com.healingapp.triphealing.network.post.ItemRecRV
import com.healingapp.triphealing.network.trip.ItemRegionRV
import com.healingapp.triphealing.network.trip.ItemSiGuRV
import com.healingapp.triphealing.secret.Secret

class SiGuAdapter(val itemList: ArrayList<ItemSiGuRV>) :
    RecyclerView.Adapter<SiGuAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sigu_region, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.tvRegion.text = itemList[position].region

        Glide.with(holder.imgRegion.context)
            .load(itemList[position].regionImage)
            .error(R.drawable.tree)
            .into(holder.imgRegion)

        holder.imgRegion.clipToOutline = true

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        // 높이를 다르게 해주기
        val params: ViewGroup.LayoutParams? = holder.layoutContainer.layoutParams
        when(position % 3){
            0,3 ->{
                params?.height = 700
            }
            1 ->{
                params?.height = 550
            }
            2->{
                params?.height = 650
            }
        }
        holder.layoutContainer.layoutParams = params
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
        val layoutContainer = itemView.findViewById<ConstraintLayout>(R.id.layoutSiGuItem)


    }

}