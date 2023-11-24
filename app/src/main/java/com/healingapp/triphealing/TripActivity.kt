package com.healingapp.triphealing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.healingapp.triphealing.databinding.ActivityTripBinding
import com.healingapp.triphealing.databinding.FragmentTripBinding

class TripActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTripBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}