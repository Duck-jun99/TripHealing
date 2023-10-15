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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.healingapp.triphealing.adapter.FamRvAdapter
import com.healingapp.triphealing.adapter.LatestRvAdapter
import com.healingapp.triphealing.MainActivity
import com.healingapp.triphealing.ProfileActivity
import com.healingapp.triphealing.R
import com.healingapp.triphealing.adapter.RecRVAdapter
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.databinding.FragmentMainBinding
import com.healingapp.triphealing.network.post.ItemRecRV
import com.healingapp.triphealing.viewmodel.post.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = (activity as MainActivity).id
        val pw = (activity as MainActivity).pw
        val token = (activity as MainActivity).token

        /// 추후에 수정해야 하는 부분
        val recRvitemList = ArrayList<ItemRecRV>()
        val recRvAdapter = RecRVAdapter(recRvitemList)

        val famRVitemList = ArrayList<ItemRecRV>()
        val famRvAdapter = FamRvAdapter(famRVitemList)

        val latestRvitemList = ArrayList<ItemRecRV>()
        val latestRvAdapter = LatestRvAdapter(latestRvitemList)

        viewModelPost = ViewModelProvider(requireActivity())[NetworkViewModel::class.java]
        viewModelUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]


        viewModelPost.getNetworkResponseLiveData().observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                //Log.e("TEST",GsonBuilder().setPrettyPrinting().create().toJson(response))
                Log.e("Test", response.size.toString())
                //binding.user.text = GsonBuilder().setPrettyPrinting().create().toJson(response)
                for(i:Int in 0 until response.size.toInt()){
                    recRvitemList.add(ItemRecRV(response[i].fieldList.title, response[i].fieldList.text,response[i].fieldList.image))
                    famRVitemList.add(ItemRecRV(response[i].fieldList.title, response[i].fieldList.text,response[i].fieldList.image))
                    latestRvitemList.add(ItemRecRV(response[i].fieldList.title, response[i].fieldList.text,response[i].fieldList.image))
                    //recRvAdapter.notifyDataSetChanged()
                    //famRvAdapter.notifyDataSetChanged()
                    //latestRvAdapter.notifyDataSetChanged()
                }
            }
        })




        viewModelUser.getNetworkUserResponseLiveData().observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.code == "0000"){
                //Log.e("TEST USER INFO", response.userInfo.toString())
                Glide.with(this)
                    .load(Secret.MEDIA_URL+response.userInfo.profileImg)
                    .error(R.drawable.group_24)
                    .into(binding.ellipse2)
                binding.user.text = HtmlCompat.fromHtml("<b>${response.userInfo.nickname}님</b>, 환영해요!<br>오늘은 이 글 어때요?", HtmlCompat.FROM_HTML_MODE_LEGACY)
            }
            else{
                Glide.with(this)
                    .load(R.drawable.group_24)
                    .error(R.drawable.group_24)
                    .into(binding.ellipse2)

                binding.user.text = HtmlCompat.fromHtml("<b>GUEST님</b>, <br>로그인하시면 더 많은 정보를 볼 수 있어요!", HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

        })


        binding.ellipse2.setOnClickListener {

            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("id",id)
            intent.putExtra("pw",pw)
            intent.putExtra("token",token)
            startActivity(intent)
        }



        //initListeners()


        //val username = arguments?.getString("username")

        //binding.user.text = HtmlCompat.fromHtml("<b>${username}님</b>, 환영해요!<br>오늘은 이 글 어때요?", HtmlCompat.FROM_HTML_MODE_LEGACY)


        binding.recRvPost.adapter = recRvAdapter
        binding.recRvPost.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        binding.recFamRvPost.adapter = famRvAdapter
        binding.recFamRvPost.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        binding.latestRvPost.adapter = latestRvAdapter
        binding.latestRvPost.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        if (::viewModelPost.isInitialized){
            viewModelPost.getNetwork()
        }
        else Toast.makeText(requireActivity(), "Server Error", Toast.LENGTH_SHORT).show()

    }

}