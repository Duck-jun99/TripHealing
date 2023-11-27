package com.healingapp.triphealing.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.JsonSyntaxException
import com.healingapp.triphealing.R
import com.healingapp.triphealing.TripDetailActivity
import com.healingapp.triphealing.adapter.TripDetailAdapter
import com.healingapp.triphealing.databinding.FragmentTripDetail2Binding
import com.healingapp.triphealing.model.trip.NetworkTripResponse
import com.healingapp.triphealing.network.trip.ItemTripDetailRV
import com.healingapp.triphealing.network.trip.TripInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripDetailFragment2: Fragment() {
    private var _binding: FragmentTripDetail2Binding? = null
    private val binding get() = _binding!!

    private val maxRetries = 3
    private var currentRetry = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTripDetail2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var codeArray = (activity as TripDetailActivity).codeArray
        var areaCode = (activity as TripDetailActivity).areaCode
        var position = (activity as TripDetailActivity).position

        Log.e("TripDetailFragment",codeArray.toString())
        Log.e("TripDetailFragment",position.toString())

        var tripDetailItem = ArrayList<ItemTripDetailRV>()
        var tripDetailAdapter = TripDetailAdapter(tripDetailItem)

        binding.rvTripDetail.adapter = tripDetailAdapter
        binding.rvTripDetail.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        for (codeItem in codeArray) {
            val parts = codeItem.split("|")
            val region = parts[0]
            val code = parts[1].toInt()

            if(position+1 == code){
                Log.e("siguRvAdapter", code.toString())
                TripInterface.create().getNetwork(
                    numOfRows=0,
                    pageNo=0,
                    MobileOS="AND",
                    MobileApp="TripHealing",
                    serviceKey=resources.getString(R.string.api_key_decoding_data_go_kr),
                    _type="json",
                    contentTypeId="32", //12:관광지 //32:숙박
                    areaCode=areaCode.toString(),
                    sigunguCode=code.toString(),
                ).enqueue(object : Callback<NetworkTripResponse> {
                    override fun onResponse(
                        call: Call<NetworkTripResponse>,
                        response: Response<NetworkTripResponse>
                    ) {
                        Log.e("TripInterface_onResponse",response.body().toString())
                        for(i:Int in 0 until response.body()!!.response.body.items.item.size){
                            var addr = response.body()!!.response.body.items.item[i].addr1
                            var title = response.body()!!.response.body.items.item[i].title
                            var img = response.body()!!.response.body.items.item[i].firstimage

                            tripDetailItem.add(ItemTripDetailRV(title,addr,img))
                            tripDetailAdapter.notifyDataSetChanged()
                        }

                    }

                    override fun onFailure(call: Call<NetworkTripResponse>, t: Throwable) {
                        //TODO("Not yet implemented")
                        Log.e("TripInterface_onFailure",t.message.toString())
                        Log.e("TripInterface_onFailure",t.toString())

                        if(t is JsonSyntaxException){
                            if(currentRetry<maxRetries){
                                retryTripRequest(areaCode.toString(),code.toString(),tripDetailItem,tripDetailAdapter)
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

    private fun retryTripRequest(areaCode:String, code:String, tripDetailItem:ArrayList<ItemTripDetailRV>, tripDetailAdapter: TripDetailAdapter){
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

                    tripDetailItem.add(ItemTripDetailRV(title,addr,img))
                    tripDetailAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<NetworkTripResponse>, t: Throwable) {
                //TODO("Not yet implemented")
                Log.e("TripInterface_onFailure",t.message.toString())

                if(t is JsonSyntaxException){
                    if(currentRetry<maxRetries){
                        retryTripRequest(areaCode.toString(),code.toString(),tripDetailItem, tripDetailAdapter)
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