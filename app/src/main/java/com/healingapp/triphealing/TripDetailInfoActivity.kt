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
import com.healingapp.triphealing.databinding.ActivityTripDetailInfoBinding
import com.healingapp.triphealing.view.TripDetailFragment1
import com.healingapp.triphealing.view.TripDetailFragment2
import com.healingapp.triphealing.view.TripDetailFragment3

class TripDetailInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTripDetailInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)



    }




}