package com.healingapp.triphealing.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.healingapp.triphealing.adapter.FamRvAdapter
import com.healingapp.triphealing.adapter.NotificationRvAdapter
import com.healingapp.triphealing.databinding.FragmentNotificationBinding
import com.healingapp.triphealing.db.NotificationDatabase
import com.healingapp.triphealing.db.NotificationEntity
import com.healingapp.triphealing.network.post.ItemFamRV
import com.healingapp.triphealing.utils.SwipeController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var db: NotificationDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNotification.text = HtmlCompat.fromHtml("<b>알림</b>",
            HtmlCompat.FROM_HTML_MODE_LEGACY)


        val notificationList = ArrayList<NotificationEntity>()
        val notiRvAdapter = context?.let { NotificationRvAdapter(it, notificationList) }

        val itemTouchHelper = notiRvAdapter?.let { SwipeController(it) }
            ?.let { ItemTouchHelper(it) }

        itemTouchHelper?.attachToRecyclerView(binding.recNotification)

        binding.recNotification.adapter = notiRvAdapter
        val manager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        manager.reverseLayout = true
        manager.stackFromEnd = true

        binding.recNotification.layoutManager = manager


        db = context?.let { NotificationDatabase.getInstance(it) }!!
        CoroutineScope(Dispatchers.Main).launch {
            val data = CoroutineScope(Dispatchers.IO).async {
                db.notificationDao().getAll()
            }.await()

            if(data != null) {
                Log.e("NotificationFragment","ROOM DATA: ${data.toString()}")
                for (i:Int in 0 until data.size.toInt()){
                    notificationList.add(data[i])
                }
                notiRvAdapter?.notifyDataSetChanged()
                notiRvAdapter?.setItemClickListener(object: NotificationRvAdapter.OnItemClickListener{
                    override fun onClick(v: View, position: Int) {
                        CoroutineScope(Dispatchers.Default).launch {
                            //수정 필요
                            db.notificationDao().checkNotification(position)

                        }

                    }
                })
            }
        }

    }
}