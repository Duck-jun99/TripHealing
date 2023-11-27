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
import com.healingapp.triphealing.network.trip.ItemTripDetailRV
import com.healingapp.triphealing.secret.Secret

class TripDetailAdapter(val itemList: ArrayList<ItemTripDetailRV>) :
    RecyclerView.Adapter<TripDetailAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trip_detail, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.tvTitle.text = itemList[position].title
        holder.tvAddr.text = itemList[position].addr

        holder.img.apply {
            Glide.with(this)
                .load(itemList[position].img)
                .error(R.drawable.tree)
                .into(this)

            clipToOutline = true
        }
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
        val tvTitle = itemView.findViewById<TextView>(R.id.tvTripTitle)
        val tvAddr = itemView.findViewById<TextView>(R.id.tvTripAddr)
        val img = itemView.findViewById<ImageView>(R.id.imgTrip)


    }

}