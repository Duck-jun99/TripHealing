package com.healingapp.triphealing.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.JsonSyntaxException
import com.healingapp.triphealing.R
import com.healingapp.triphealing.TripDetailActivity
import com.healingapp.triphealing.adapter.TripAnotherAdapter
import com.healingapp.triphealing.databinding.FragmentTripDetailInfoBinding
import com.healingapp.triphealing.model.trip.NetworkTripDetailInfoResponse
import com.healingapp.triphealing.model.trip.NetworkTripResponse
import com.healingapp.triphealing.network.trip.ItemTripDetailRV
import com.healingapp.triphealing.network.trip.TripInterface
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripDetailInfoFragment: Fragment() {
    private var _binding: FragmentTripDetailInfoBinding? = null
    private lateinit var callback: OnBackPressedCallback
    private val binding get() = _binding!!

    private val maxRetries = 3
    private var currentRetry = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 클릭시 동작하는 로직
                requireActivity().supportFragmentManager.beginTransaction()
                    //.setCustomAnimations(0, R.anim.horizon_exit_front)
                    .remove(this@TripDetailInfoFragment)
                    .commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTripDetailInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapView = MapView(requireContext())
        val marker = MapPOIItem()

        var codeArray = (activity as TripDetailActivity).codeArray
        var areaCode = (activity as TripDetailActivity).areaCode
        var position = (activity as TripDetailActivity).position
        var contentId = ""
        var regionName = (activity as TripDetailActivity).regionName

        binding.tvAnotherTrip.text=HtmlCompat.fromHtml("<b style=\"color:#FA7E56;\">${regionName}</b>의 다른 관광지는 어때요?",HtmlCompat.FROM_HTML_MODE_LEGACY)

        Log.e("TripDetailFragment",codeArray.toString())
        Log.e("TripDetailFragment",position.toString())


        setFragmentResultListener("requestKey") { requestKey, bundle ->
            contentId = bundle.getString("contentId").toString()
            Log.e("TripDetailInfoFragment",contentId)

            for (codeItem in codeArray) {
                val parts = codeItem.split("|")
                val region = parts[0]
                val code = parts[1].toInt()

                if(position+1 == code){
                    Log.e("siguRvAdapter", code.toString())
                    TripInterface.create().getDetailNetwork(
                        MobileOS = "AND",
                        MobileApp = "TripHealing",
                        serviceKey = resources.getString(R.string.api_key_decoding_data_go_kr),
                        _type = "json",
                        contentId =contentId, //TripDetailFragment에서 contentId넘겨줌.
                        defaultYN = "Y",
                        firstImageYN = "Y",
                        areacodeYN = "Y",
                        addrinfoYN = "Y",
                        mapinfoYN = "Y",
                        overviewYN = "Y"
                        /*numOfRows=0,
                        pageNo=0,
                        MobileOS="AND",
                        MobileApp="TripHealing",
                        serviceKey=resources.getString(R.string.api_key_decoding_data_go_kr),
                        _type="json",
                        contentTypeId="12", //12:관광지 14:문화시설 32:숙박
                        areaCode=areaCode.toString(),
                        sigunguCode=code.toString(),

                         */
                    ).enqueue(object : Callback<NetworkTripDetailInfoResponse> {
                        override fun onResponse(
                            call: Call<NetworkTripDetailInfoResponse>,
                            response: Response<NetworkTripDetailInfoResponse>
                        ) {
                            Log.e("TripInterface_onResponse",response.body().toString())
                            binding.tvTitleTripDetailInfo.text = HtmlCompat.fromHtml(response.body()!!.response.body.items.item[0].title,HtmlCompat.FROM_HTML_MODE_LEGACY)
                            binding.imgTripDetailInfo.apply {
                                Glide.with(this)
                                    .load(response.body()!!.response.body.items.item[0].firstimage)
                                    .error(R.drawable.tree)
                                    .into(this)
                            }
                            binding.tvTextTripDetailInfo.text = HtmlCompat.fromHtml(response.body()!!.response.body.items.item[0].overview,HtmlCompat.FROM_HTML_MODE_LEGACY)
                            binding.tvAddrTripDetailInfo.text = response.body()!!.response.body.items.item[0].addr1
                            //binding.tvHomepageTripDetailInfo.text = HtmlCompat.fromHtml(response.body()!!.response.body.items.item[0].homepage,HtmlCompat.FROM_HTML_MODE_LEGACY)

                            if((response.body()!!.response.body.items.item[0].mapy != null) and (response.body()!!.response.body.items.item[0].mapx != null)){
                                Log.e("Not Null", "Not Null")
                                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(response.body()!!.response.body.items.item[0].mapy.toDouble(), response.body()!!.response.body.items.item[0].mapx.toDouble()), false)
                                marker.itemName = response.body()!!.response.body.items.item[0].title
                                marker.tag = 0
                                marker.mapPoint = MapPoint.mapPointWithGeoCoord(response.body()!!.response.body.items.item[0].mapy.toDouble(), response.body()!!.response.body.items.item[0].mapx.toDouble())
                                marker.markerType = MapPOIItem.MarkerType.RedPin

                                mapView.addPOIItem(marker)
                                binding.mapView.addView(mapView)
                            }

                        }

                        override fun onFailure(call: Call<NetworkTripDetailInfoResponse>, t: Throwable) {
                            //TODO("Not yet implemented")
                            Log.e("TripInterface_onFailure",t.message.toString())
                            Log.e("TripInterface_onFailure",t.toString())

                            if(t is JsonSyntaxException){
                                if(currentRetry<maxRetries){
                                    retryTripRequest(areaCode.toString(),code.toString(),contentId,mapView,marker)
                                    currentRetry++
                                }
                                else{
                                    Toast.makeText(requireContext(),"서버통신 실패", Toast.LENGTH_SHORT).show()
                                }
                            }

                        }

                    })
                }

            }

        }

        setFragmentResultListener("requestKey2") {requestKey2, bundle ->


            try{
                var anotherRegionItem = bundle.getParcelableArrayList("anotherRegion",ItemTripDetailRV::class.java)
                Log.e("setFragmentResultListener",anotherRegionItem.toString())

                var tripAnotherAdapter = anotherRegionItem?.let { TripAnotherAdapter(it) }

                binding.rvAnotherTrip.adapter = tripAnotherAdapter
                binding.rvAnotherTrip.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                tripAnotherAdapter!!.notifyDataSetChanged()

            }catch (e: IllegalArgumentException){
                Toast.makeText(requireContext(),"안드로이드를 업데이트해주세요!",Toast.LENGTH_SHORT).show()
                binding.tvAnotherTrip.text=HtmlCompat.fromHtml("안드로이드 버전을 업데이트해주세요.",HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
            catch (e: NoSuchMethodError){
                Toast.makeText(requireContext(),"안드로이드를 업데이트해주세요!",Toast.LENGTH_SHORT).show()
                binding.tvAnotherTrip.text=HtmlCompat.fromHtml("안드로이드 버전을 업데이트해주세요.",HtmlCompat.FROM_HTML_MODE_LEGACY)
            }


        }

        //MapView 스크롤을 위해(ScrollView에 움직임 뺏기지 않게 함)
        mapView.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_MOVE -> binding.scrollView.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> binding.scrollView.requestDisallowInterceptTouchEvent(
                    false
                )
            }
            mapView.onTouchEvent(event)
        }

    }

    private fun retryTripRequest(areaCode:String, code:String, contentId:String, mapView:MapView, marker:MapPOIItem){
        TripInterface.create().getDetailNetwork(
            MobileOS = "AND",
            MobileApp = "TripHealing",
            serviceKey = resources.getString(R.string.api_key_decoding_data_go_kr),
            _type = "json",
            contentId =contentId, //TripDetailFragment에서 contentId넘겨줌.
            defaultYN = "Y",
            firstImageYN = "Y",
            areacodeYN = "Y",
            addrinfoYN = "Y",
            mapinfoYN = "Y",
            overviewYN = "Y"
            /*numOfRows=0,
            pageNo=0,
            MobileOS="AND",
            MobileApp="TripHealing",
            serviceKey=resources.getString(R.string.api_key_decoding_data_go_kr),
            _type="json",
            contentTypeId="12", //12:관광지 14:문화시설 32:숙박
            areaCode=areaCode.toString(),
            sigunguCode=code.toString(),

             */
        ).enqueue(object : Callback<NetworkTripDetailInfoResponse> {
            override fun onResponse(
                call: Call<NetworkTripDetailInfoResponse>,
                response: Response<NetworkTripDetailInfoResponse>
            ) {
                Log.e("TripInterface_onResponse",response.body().toString())
                currentRetry = 0
                binding.tvTitleTripDetailInfo.text = response.body()!!.response.body.items.item[0].title
                binding.imgTripDetailInfo.apply {
                    Glide.with(this)
                        .load(response.body()!!.response.body.items.item[0].firstimage)
                        .error(R.drawable.tree)
                        .into(this)
                }
                binding.tvTextTripDetailInfo.text = HtmlCompat.fromHtml(response.body()!!.response.body.items.item[0].overview,HtmlCompat.FROM_HTML_MODE_LEGACY)
                binding.tvAddrTripDetailInfo.text = response.body()!!.response.body.items.item[0].addr1
                //binding.tvHomepageTripDetailInfo.text = HtmlCompat.fromHtml(response.body()!!.response.body.items.item[0].homepage,HtmlCompat.FROM_HTML_MODE_LEGACY)

                if((response.body()!!.response.body.items.item[0].mapy != null) and (response.body()!!.response.body.items.item[0].mapx != null)){
                    Log.e("Not Null", "Not Null")
                    mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(response.body()!!.response.body.items.item[0].mapy.toDouble(), response.body()!!.response.body.items.item[0].mapx.toDouble()), false)
                    marker.itemName = response.body()!!.response.body.items.item[0].title
                    marker.tag = 0
                    marker.mapPoint = MapPoint.mapPointWithGeoCoord(response.body()!!.response.body.items.item[0].mapy.toDouble(), response.body()!!.response.body.items.item[0].mapx.toDouble())
                    marker.markerType = MapPOIItem.MarkerType.RedPin

                    mapView.addPOIItem(marker)
                    binding.mapView.addView(mapView)
                }

            }

            override fun onFailure(call: Call<NetworkTripDetailInfoResponse>, t: Throwable) {
                //TODO("Not yet implemented")
                Log.e("TripInterface_onFailure",t.message.toString())
                Log.e("TripInterface_onFailure",t.toString())

                if(t is JsonSyntaxException){
                    if(currentRetry<maxRetries){
                        retryTripRequest(areaCode.toString(),code.toString(),contentId,mapView,marker)
                        currentRetry++
                    }
                    else{
                        Toast.makeText(requireContext(),"서버통신 실패", Toast.LENGTH_SHORT).show()
                    }
                }


            }

        })
    }
}