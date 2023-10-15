package com.healingapp.triphealing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.healingapp.triphealing.network.post.ItemRecRV

class FamRvAdapter(val itemList: ArrayList<ItemRecRV>) :
    RecyclerView.Adapter<FamRvAdapter.BoardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_famrv_post, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.tv_title.text = itemList[position].title
        holder.tv_author.text = itemList[position].author
        Glide.with(holder.img.context)
            .load(
            Secret.MEDIA_URL+itemList[position].image)
            .into(holder.img)
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }


    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_title = itemView.findViewById<TextView>(R.id.tv_fam_rv_title)
        val tv_author = itemView.findViewById<TextView>(R.id.tv_fam_rv_author)
        val img = itemView.findViewById<ImageView>(R.id.img_fam_rv)


    }

}