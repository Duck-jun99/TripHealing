package com.healingapp.triphealing.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.healingapp.triphealing.R
import com.healingapp.triphealing.db.TripDatabase
import com.healingapp.triphealing.db.NotificationEntity
import com.healingapp.triphealing.utils.ItemTouchHelperListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationRvAdapter(private val context: Context, val itemList: ArrayList<NotificationEntity>) :
    RecyclerView.Adapter<NotificationRvAdapter.BoardViewHolder>(), ItemTouchHelperListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return BoardViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            if (TripDatabase.getInstance(holder.back.context)?.notificationDao()
                    ?.getNotification(position + 1) != 1
            ) {
                holder.back.setBackgroundColor(Color.WHITE)
            } else {
                holder.back.setBackgroundColor(Color.parseColor("#FEE3CF"))
            }
        }
        holder.tv_title.text = itemList[position].title
        holder.tv_body.text = itemList[position].body
        Glide.with(holder.img.context)
            .load(
                R.drawable.group_24
            )
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

    private lateinit var itemClickListener: OnItemClickListener


    inner class BoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_title = itemView.findViewById<TextView>(R.id.tv_noti_title)
        val tv_body = itemView.findViewById<TextView>(R.id.tv_noti_body)
        val img = itemView.findViewById<ImageView>(R.id.img_noti)
        val back = itemView.findViewById<LinearLayout>(R.id.view_noti)

    }

    override fun onLeftClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {

    }

    override fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder?) {
        CoroutineScope(Dispatchers.IO).launch {
            TripDatabase.getInstance(context)?.notificationDao()
                ?.deleteNotification(position)
        }
    }

    override fun onItemMove(from_position: Int, to_position: Int): Boolean = false

    override fun onItemSwipe(position: Int) {

    }


}
