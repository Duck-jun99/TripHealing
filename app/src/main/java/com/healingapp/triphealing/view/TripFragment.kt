package com.healingapp.triphealing.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.gson.JsonSyntaxException
import com.healingapp.triphealing.R
import com.healingapp.triphealing.TripDetailActivity
import com.healingapp.triphealing.adapter.RegionAdapter
import com.healingapp.triphealing.adapter.SiGuAdapter
import com.healingapp.triphealing.databinding.FragmentTripBinding
import com.healingapp.triphealing.model.trip.NetworkTripResponse
import com.healingapp.triphealing.network.trip.ItemRegionRV
import com.healingapp.triphealing.network.trip.ItemSiGuRV
import com.healingapp.triphealing.network.trip.TripInterface
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

        regionRvitemList.add(ItemRegionRV("서울",R.drawable.seoul))
        regionRvitemList.add(ItemRegionRV("인천",R.drawable.incheon))
        regionRvitemList.add(ItemRegionRV("대전",R.drawable.daejeon))
        regionRvitemList.add(ItemRegionRV("대구",R.drawable.daegu))
        regionRvitemList.add(ItemRegionRV("광주",R.drawable.gwangju))
        regionRvitemList.add(ItemRegionRV("부산",R.drawable.busan))
        regionRvitemList.add(ItemRegionRV("울산",R.drawable.ulsan))
        regionRvitemList.add(ItemRegionRV("세종",R.drawable.sejong))
        regionRvitemList.add(ItemRegionRV("경기도",R.drawable.gyeonggi))
        regionRvitemList.add(ItemRegionRV("강원도",R.drawable.gangwon))
        regionRvitemList.add(ItemRegionRV("충청북도",R.drawable.chungcheongbuk))
        regionRvitemList.add(ItemRegionRV("충청남도",R.drawable.chungcheongnam))
        regionRvitemList.add(ItemRegionRV("경상북도",R.drawable.gyeongsangbuk))
        regionRvitemList.add(ItemRegionRV("경상남도",R.drawable.gyeongsangnam))
        regionRvitemList.add(ItemRegionRV("전라북도",R.drawable.jeollabuk))
        regionRvitemList.add(ItemRegionRV("전라남도",R.drawable.jeollanam))
        regionRvitemList.add(ItemRegionRV("제주도",R.drawable.jeju))

        var siguRvitemList = ArrayList<ItemSiGuRV>()
        var siguRvAdapter = SiGuAdapter(siguRvitemList)

        var codeArray = ArrayList<String>()
        var areaCode:Int = 0

        binding.rvSiGu.adapter = siguRvAdapter
        binding.rvSiGu.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        regionRvAdapter.setItemClickListener(object : RegionAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                Log.e("regionRvAdapter", position.toString())

                //서울인 경우
                when (position) {
                    0 -> {
                        siguRvitemList.clear()
                        codeArray.clear()
                        //var siguArray = resources.getStringArray(R.array.서울)

                        //서울의 areacode는 1
                        areaCode = 1
                        for (codeItem in resources.getStringArray(R.array.seoul_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.seoul))
                        }
                        //Log.e("regionRvAdapter", siguRvitemList.toString())
                        //Log.e("regionRvAdapter", codeArray.toString())

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //인천인 경우
                    1 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        //인천의 areacode는 2
                        areaCode = 2
                        for (codeItem in resources.getStringArray(R.array.incheon_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.incheon))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //대전인 경우
                    2 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 3
                        for (codeItem in resources.getStringArray(R.array.daejeon_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.daejeon))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //대구인 경우
                    3 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 4
                        for (codeItem in resources.getStringArray(R.array.daegu_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.daegu))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //광주인 경우
                    4 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 5
                        for (codeItem in resources.getStringArray(R.array.gwangju_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.gwangju))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //부산인 경우
                    5 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 6
                        for (codeItem in resources.getStringArray(R.array.busan_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.busan))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //울산인 경우
                    6 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 7
                        for (codeItem in resources.getStringArray(R.array.ulsan_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.ulsan))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //세종인 경우
                    7 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 8
                        for (codeItem in resources.getStringArray(R.array.sejong_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.sejong))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //경기도인 경우
                    8 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 31
                        for (codeItem in resources.getStringArray(R.array.gyeonggi_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.gyeonggi))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //강원도인 경우
                    9 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 32
                        for (codeItem in resources.getStringArray(R.array.gangwon_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.gangwon))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //충청북도인 경우
                    10 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 33
                        for (codeItem in resources.getStringArray(R.array.chungcheongbuk_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.chungcheongbuk))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //충청남도인 경우
                    11 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 34
                        for (codeItem in resources.getStringArray(R.array.chungcheongnam_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.chungcheongnam))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //경상북도인 경우
                    12 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 35
                        for (codeItem in resources.getStringArray(R.array.gyeongsangbuk_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.gyeongsangbuk))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //경상남도인 경우
                    13 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 36
                        for (codeItem in resources.getStringArray(R.array.gyeongsangnam_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.gyeongsangnam))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //전라북도인 경우
                    14 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 37
                        for (codeItem in resources.getStringArray(R.array.jeollabuk_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.jeollabuk))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //전라남도인 경우
                    15 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 38
                        for (codeItem in resources.getStringArray(R.array.jeollanam_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.jeollanam))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                    //제주도인 경우
                    16 -> {
                        siguRvitemList.clear()
                        codeArray.clear()

                        areaCode = 39
                        for (codeItem in resources.getStringArray(R.array.jeju_codes)) {
                            codeArray.add(codeItem)
                            //<item>강남구|1</item> 형식으로 저장되어 있기 때문에
                            val parts = codeItem.split("|")
                            //region = 시 또는 구
                            val region = parts[0]
                            val code = parts[1].toInt()
                            siguRvitemList.add(ItemSiGuRV(region = region, regionImage = R.drawable.jeju))
                        }

                        siguRvAdapter.notifyDataSetChanged()
                    }
                }
            }

        })

        siguRvAdapter.setItemClickListener(object : SiGuAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                var intent = Intent(context, TripDetailActivity::class.java)
                intent.putStringArrayListExtra("codeArray",codeArray)
                intent.putExtra("areaCode",areaCode)
                intent.putExtra("position",position)
                startActivity(intent)
            }

        })

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