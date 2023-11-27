package com.healingapp.triphealing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.healingapp.triphealing.databinding.ActivityTripBinding
import com.healingapp.triphealing.view.TripDetailFragment1
import com.healingapp.triphealing.view.TripDetailFragment2
import com.healingapp.triphealing.view.TripDetailFragment3

class TripDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTripBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    lateinit var codeArray:ArrayList<String>
    var areaCode:Int = 0
    var position:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        codeArray = intent.getStringArrayListExtra("codeArray")!!
        areaCode = intent.getIntExtra("areaCode",0)
        position = intent.getIntExtra("position",0)

        initViewPager()

    }

    private fun initViewPager() {
        //ViewPager2 Adapter 셋팅
        var viewPager2Adatper = ViewPager2Adapter(this)
        viewPager2Adatper.addFragment(TripDetailFragment1())
        viewPager2Adatper.addFragment(TripDetailFragment2())
        viewPager2Adatper.addFragment(TripDetailFragment3())

        //Adapter 연결
        binding.viewPager2Container.apply {
            adapter = viewPager2Adatper

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }

        //ViewPager, TabLayout 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager2Container) { tab, position ->
            Log.e("YMC", "ViewPager position: ${position}")
            when (position) {
                0 -> tab.text = "관광지"
                1 -> tab.text = "문화시설"
                2 -> tab.text = "숙박시설"
            }
        }.attach()
    }

}

//ViewPager2Adapter.kt
class ViewPager2Adapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    var fragments: ArrayList<Fragment> = ArrayList()

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.size - 1)
        //TODO: notifyItemInserted!!
    }

    fun removeFragement() {
        fragments.removeLast()
        notifyItemRemoved(fragments.size)
        //TODO: notifyItemRemoved!!
    }

}