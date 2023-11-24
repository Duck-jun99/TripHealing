package com.healingapp.triphealing.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.healingapp.triphealing.PostActivity
import com.healingapp.triphealing.R
import com.healingapp.triphealing.adapter.MbtiRvAdapter
import com.healingapp.triphealing.adapter.RecRVAdapter
import com.healingapp.triphealing.adapter.RegionAdapter
import com.healingapp.triphealing.adapter.SiGuAdapter
import com.healingapp.triphealing.databinding.FragmentSettingBinding
import com.healingapp.triphealing.databinding.FragmentTripBinding
import com.healingapp.triphealing.model.trip.NetworkTripResponse
import com.healingapp.triphealing.network.post.ItemRecRV
import com.healingapp.triphealing.network.trip.ItemRegionRV
import com.healingapp.triphealing.network.trip.ItemSiGuRV
import com.healingapp.triphealing.network.trip.TripInterface
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.viewmodel.post_all.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripFragment : Fragment() {

    private var _binding: FragmentTripBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTripBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val regionRvitemList = ArrayList<ItemRegionRV>()
        val regionRvAdapter = RegionAdapter(regionRvitemList)
        binding.rvRegion.adapter = regionRvAdapter
        binding.rvRegion.layoutManager = GridLayoutManager(requireActivity(),1, GridLayoutManager.HORIZONTAL, false)

        regionRvitemList.add(ItemRegionRV("서울",""))
        regionRvitemList.add(ItemRegionRV("인천",""))
        regionRvitemList.add(ItemRegionRV("부산",""))
        regionRvitemList.add(ItemRegionRV("광주",""))
        regionRvitemList.add(ItemRegionRV("대구",""))
        regionRvitemList.add(ItemRegionRV("대전",""))
        regionRvitemList.add(ItemRegionRV("울산",""))
        regionRvitemList.add(ItemRegionRV("세종",""))
        regionRvitemList.add(ItemRegionRV("경기도",""))
        regionRvitemList.add(ItemRegionRV("강원도",""))
        regionRvitemList.add(ItemRegionRV("충청북도",""))
        regionRvitemList.add(ItemRegionRV("충청남도",""))
        regionRvitemList.add(ItemRegionRV("전라북도",""))
        regionRvitemList.add(ItemRegionRV("전라남도",""))
        regionRvitemList.add(ItemRegionRV("경상북도",""))
        regionRvitemList.add(ItemRegionRV("경상남도",""))
        regionRvitemList.add(ItemRegionRV("제주도",""))

        var siguRvitemList = ArrayList<ItemSiGuRV>()
        var siguRvAdapter = SiGuAdapter(siguRvitemList)

        var codeArray:Array<String>

        binding.rvSiGu.adapter = siguRvAdapter
        binding.rvSiGu.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        regionRvAdapter.setItemClickListener(object : RegionAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                Log.e("regionRvAdapter", position.toString())

                //서울인 경우만 구현
                if (position == 0) {
                    siguRvitemList.clear()
                    var siguArray = resources.getStringArray(R.array.서울)
                    codeArray = resources.getStringArray(R.array.서울코드)
                    for (i: Int in siguArray.indices) {
                        siguRvitemList.add(ItemSiGuRV(siguArray[i], ""))
                    }
                    Log.e("regionRvAdapter", siguRvitemList.toString())

                    // Notify the adapter that the data has changed
                    siguRvAdapter.notifyDataSetChanged()
                }
            }
        })

        siguRvAdapter.setItemClickListener(object : SiGuAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                TripInterface.create().getNetwork(
                    numOfRows=0,
                    pageNo=0,
                    MobileOS="AND",
                    MobileApp="TripHealing",
                    serviceKey=resources.getString(R.string.api_key_encoding_data_go_kr),
                    _type="json",
                    contentTypeId="12", //12:관광지
                    areaCode="1",
                    sigunguCode="1",
                ).enqueue(object :Callback<NetworkTripResponse>{
                    override fun onResponse(
                        call: Call<NetworkTripResponse>,
                        response: Response<NetworkTripResponse>
                    ) {
                        Log.e("TripInterface",response.body().toString())
                    }

                    override fun onFailure(call: Call<NetworkTripResponse>, t: Throwable) {
                        //TODO("Not yet implemented")
                    }

                })
            }

        })


        viewModelPost = ViewModelProvider(requireActivity())[NetworkViewModel::class.java]
        viewModelUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        viewModelUser.getNetworkUserResponseLiveData().observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.code == "0000"){
                binding.tvTrip.text = HtmlCompat.fromHtml("<b>${response.userInfo.nickname}님!</b> 어디로 떠날까요?", HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

            else{
                binding.tvTrip.text = HtmlCompat.fromHtml("<b>Guest님!<b> 어디로 떠날까요?", HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

        })


    }

}