package com.healingapp.triphealing.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import com.healingapp.triphealing.adapter.TripDetailAdapter
import com.healingapp.triphealing.databinding.FragmentTripDetail1Binding
import com.healingapp.triphealing.databinding.FragmentTripDetailInfoBinding
import com.healingapp.triphealing.model.trip.NetworkTripDetailInfoResponse
import com.healingapp.triphealing.model.trip.NetworkTripResponse
import com.healingapp.triphealing.network.trip.ItemTripDetailRV
import com.healingapp.triphealing.network.trip.TripInterface
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var codeArray = (activity as TripDetailActivity).codeArray
        var areaCode = (activity as TripDetailActivity).areaCode
        var position = (activity as TripDetailActivity).position
        var contentId = ""

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

                        }

                        override fun onFailure(call: Call<NetworkTripDetailInfoResponse>, t: Throwable) {
                            //TODO("Not yet implemented")
                            Log.e("TripInterface_onFailure",t.message.toString())
                            Log.e("TripInterface_onFailure",t.toString())

                            if(t is JsonSyntaxException){
                                if(currentRetry<maxRetries){
                                    retryTripRequest(areaCode.toString(),code.toString())
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


    }

    private fun retryTripRequest(areaCode:String, code:String){
        TripInterface.create().getNetwork(
            numOfRows=0,
            pageNo=0,
            MobileOS="AND",
            MobileApp="TripHealing",
            serviceKey=resources.getString(R.string.api_key_encoding_data_go_kr),
            _type="json",
            contentTypeId="12", //12:관광지
            areaCode=areaCode.toString(),
            sigunguCode=code.toString(),
        ).enqueue(object :Callback<NetworkTripResponse>{
            override fun onResponse(
                call: Call<NetworkTripResponse>,
                response: Response<NetworkTripResponse>
            ) {
                currentRetry=0
                Log.e("TripInterface_onResponse",response.body().toString())
                for(i:Int in 0 until response.body()!!.response.body.items.item.size){
                    var addr = response.body()!!.response.body.items.item[i].addr1
                    var title = response.body()!!.response.body.items.item[i].title
                    var img = response.body()!!.response.body.items.item[i].firstimage

                }
            }

            override fun onFailure(call: Call<NetworkTripResponse>, t: Throwable) {
                //TODO("Not yet implemented")
                Log.e("TripInterface_onFailure",t.message.toString())

                if(t is JsonSyntaxException){
                    if(currentRetry<maxRetries){
                        retryTripRequest(areaCode.toString(),code.toString())
                        currentRetry++
                    }
                    else{
                        Toast.makeText(requireContext(),"서버통신 실패",Toast.LENGTH_SHORT).show()
                    }
                }


            }

        })
    }
}