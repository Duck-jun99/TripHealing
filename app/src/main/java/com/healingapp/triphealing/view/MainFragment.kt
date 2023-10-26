package com.healingapp.triphealing.view

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.healingapp.triphealing.PostActivity
import com.healingapp.triphealing.WriteActivity
import com.healingapp.triphealing.adapter.FamRvAdapter
import com.healingapp.triphealing.adapter.LatestRvAdapter
import com.healingapp.triphealing.ProfileActivity
import com.healingapp.triphealing.R
import com.healingapp.triphealing.adapter.MbtiRvAdapter
import com.healingapp.triphealing.adapter.RecRVAdapter
import com.healingapp.triphealing.adapter.RecWriterRvAdapter
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.databinding.FragmentMainBinding
import com.healingapp.triphealing.model.post.NetworkRecWriterResponse
import com.healingapp.triphealing.model.post.NetworkResponse
import com.healingapp.triphealing.network.post.ItemFamRV
import com.healingapp.triphealing.network.post.ItemMbtiRV
import com.healingapp.triphealing.network.post.ItemRecRV
import com.healingapp.triphealing.network.post_detail.PostDetailInterface
import com.healingapp.triphealing.network.post_mbti.PostMBTIInterface
import com.healingapp.triphealing.viewmodel.post_all.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel

    // Fab 버튼 default는 닫히게 설정
    private var isFabOpen = false

    //사용자와 MBTI가 같은 유저의 게시물 interface
    val postMBTIInterface by lazy { PostMBTIInterface.create() }


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

        //플로팅버튼 이벤트 구현
        setFABClickEvent()

        /// 추후에 수정해야 하는 부분
        val recRvitemList = ArrayList<ItemRecRV>()
        val recRvAdapter = RecRVAdapter(recRvitemList)

        val famRVitemList = ArrayList<ItemFamRV>()
        val famRvAdapter = FamRvAdapter(famRVitemList)

        val latestRvitemList = ArrayList<ItemRecRV>()
        val latestRvAdapter = LatestRvAdapter(latestRvitemList)

        val recWriterRvitemList = ArrayList<ItemFamRV>()
        val recWriterRvAdapter = RecWriterRvAdapter(recWriterRvitemList)

        val mbtiRvitemList = ArrayList<ItemMbtiRV>()
        val mbtiRvAdapter = MbtiRvAdapter(mbtiRvitemList)


        viewModelPost = ViewModelProvider(requireActivity())[NetworkViewModel::class.java]
        viewModelUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]


        viewModelUser.getNetworkUserResponseLiveData().observe(viewLifecycleOwner, Observer { response ->
            if (response != null && response.code == "0000"){
                //Log.e("TEST USER INFO", response.userInfo.toString())

                Glide.with(this)
                    .load(Secret.USER_MEDIA_URL+response.userInfo.profileImg)
                    .error(R.drawable.group_24)
                    .into(binding.ellipse2)
                binding.user.text = HtmlCompat.fromHtml("<b>${response.userInfo.nickname}님</b>, 환영해요!<br>오늘은 이 글 어때요?", HtmlCompat.FROM_HTML_MODE_LEGACY)

                binding.layoutMain3.tvMbti.text = HtmlCompat.fromHtml("<b>${response.userInfo.propensity.mbti}<b>인 다른 유저 글은 어때요?", HtmlCompat.FROM_HTML_MODE_LEGACY)
                //로그인 시, 유저의 mbti와 맞는 다른 유저의 게시물 보여줌.
                postMBTIInterface.getNetwork(response.userInfo.propensity.mbti)
                    .enqueue(object : Callback<List<NetworkRecWriterResponse>> {
                        //서버 요청 성공
                        override fun onResponse(
                            call: Call<List<NetworkRecWriterResponse>>,
                            responseMbti: Response<List<NetworkRecWriterResponse>>
                        ) {
                            if(responseMbti.body()!=null){
                                for(i:Int in 0 until responseMbti.body()!!.size.toInt()){
                                    mbtiRvitemList.add(ItemMbtiRV(responseMbti.body()!![i].title, responseMbti.body()!![i].nickname,responseMbti.body()!![i].coverImage, responseMbti.body()!![i].text))
                                }
                                mbtiRvAdapter.notifyDataSetChanged()
                                Log.e("MBTILIST",mbtiRvitemList.toString())

                                mbtiRvAdapter.setItemClickListener(object: MbtiRvAdapter.OnItemClickListener{
                                    override fun onClick(v: View, position: Int) {

                                        var intent = Intent(context, PostActivity::class.java)
                                        intent.putExtra("id",responseMbti.body()!![position].id)
                                        startActivity(intent)

                                    }
                                })
                            }

                        }

                        override fun onFailure(call: Call<List<NetworkRecWriterResponse>>, t: Throwable) {
                            Log.e("POST MBTI ERROR", "onFailure: error. cause: ${t.message}")

                        }
                    })
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
            startActivity(intent)
        }


        binding.recRvPost.adapter = recRvAdapter
        binding.recRvPost.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

        binding.recFamRvPost.adapter = famRvAdapter
        binding.recFamRvPost.layoutManager = GridLayoutManager(requireActivity(),4, GridLayoutManager.HORIZONTAL, false)

        binding.latestRvPost.adapter = latestRvAdapter
        binding.latestRvPost.layoutManager = GridLayoutManager(requireActivity(), 4, GridLayoutManager.HORIZONTAL, false)

        binding.layoutMain2.rvPickWriter.adapter = recWriterRvAdapter
        binding.layoutMain2.rvPickWriter.layoutManager = GridLayoutManager(requireActivity(), 4, GridLayoutManager.HORIZONTAL, false)


        binding.layoutMain3.rvRecMbtiPost.adapter = mbtiRvAdapter
        binding.layoutMain3.rvRecMbtiPost.layoutManager = GridLayoutManager(requireActivity(), 1, GridLayoutManager.HORIZONTAL, false)


        viewModelPost.getNetworkResponseLiveData().observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {

                Log.e("Test", response.size.toString())
                //binding.user.text = GsonBuilder().setPrettyPrinting().create().toJson(response)
                for(i:Int in 0 until response.size.toInt()){

                    latestRvitemList.add(ItemRecRV(response[i].title, response[i].nickname,response[i].coverImage))

                }

                latestRvAdapter.setItemClickListener(object: LatestRvAdapter.OnItemClickListener{
                    override fun onClick(v: View, position: Int) {

                        Log.e("TEST ITEM", response[position].toString())
                        var intent = Intent(context, PostActivity::class.java)
                        intent.putExtra("id",response[position].id)
                        startActivity(intent)

                    }
                })

            }
        })

        viewModelPost.getPostFamousLiveData().observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                //Log.e("TEST",GsonBuilder().setPrettyPrinting().create().toJson(response))
                Log.e("Test", response.size.toString())
                //binding.user.text = GsonBuilder().setPrettyPrinting().create().toJson(response)
                for(i:Int in 0 until response.size.toInt()){
                    famRVitemList.add(ItemFamRV(response[i].title, response[i].nickname,response[i].coverImage,response[i].views))
                }

                famRvAdapter.setItemClickListener(object: FamRvAdapter.OnItemClickListener{
                    override fun onClick(v: View, position: Int) {

                        Log.e("TEST ITEM", response[position].toString())
                        var intent = Intent(context, PostActivity::class.java)
                        intent.putExtra("id",response[position].id)
                        startActivity(intent)

                    }
                })

            }

        })

        viewModelPost.getPostRecLiveData().observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {

                //Log.e("TEST",GsonBuilder().setPrettyPrinting().create().toJson(response))
                Log.e("Test", response.size.toString())
                //binding.user.text = GsonBuilder().setPrettyPrinting().create().toJson(response)
                for(i:Int in 0 until response.size.toInt()){
                    recRvitemList.add(ItemRecRV(response[i].title, response[i].nickname,response[i].coverImage))
                }

                recRvAdapter.setItemClickListener(object: RecRVAdapter.OnItemClickListener{
                    override fun onClick(v: View, position: Int) {

                        Log.e("TEST ITEM", response[position].toString())
                        var intent = Intent(context, PostActivity::class.java)
                        intent.putExtra("id",response[position].id)
                        startActivity(intent)

                    }
                })
            }

        })

        viewModelPost.getPostRecWriterLiveData().observe(viewLifecycleOwner, Observer { response ->
            if(response != null){

                Glide.with(this)
                    .load(Secret.MEDIA_URL+response[0].profileImg)
                    .error(R.drawable.group_24)
                    .into(binding.layoutMain2.imgPickUser)
                binding.layoutMain2.tvPickUser.text = response[0].nickname
                Glide.with(this)
                    .load(Secret.MEDIA_URL+response[0].coverImage)
                    .error(R.drawable.background2)
                    .into(binding.layoutMain2.imgCoverPick)
                binding.layoutMain2.tvTitleBestView.text = HtmlCompat.fromHtml("<b>${response[0].nickname}님</b>의 이 작품을 추천드려요.", HtmlCompat.FROM_HTML_MODE_LEGACY)
                binding.layoutMain2.tvTitle.text = response[0].title
                binding.layoutMain2.tvTitle2.text = response[0].title
                binding.layoutMain2.tvAnotherPost.text = HtmlCompat.fromHtml("<b>${response[0].nickname}님</b>의 다른 글", HtmlCompat.FROM_HTML_MODE_LEGACY)


                for(i:Int in 0 until response.size.toInt()){
                    recWriterRvitemList.add(ItemFamRV(response[i].title, response[i].nickname,response[i].coverImage,response[i].views))
                }

                recWriterRvAdapter.setItemClickListener(object: RecWriterRvAdapter.OnItemClickListener{
                    override fun onClick(v: View, position: Int) {

                        Log.e("TEST ITEM", response[position].toString())
                        var intent = Intent(context, PostActivity::class.java)
                        intent.putExtra("id",response[position].id)
                        startActivity(intent)

                    }
                })
            }
        })




        //수정 필요
        binding.layoutRefresh.setOnRefreshListener {

            viewModelPost = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[NetworkViewModel::class.java]
            viewModelUser = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))[UserViewModel::class.java]

            initListeners()

            latestRvitemList.clear()
            famRVitemList.clear()
            recRvitemList.clear()


            binding.recRvPost.adapter = recRvAdapter
            binding.recRvPost.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)

            binding.recFamRvPost.adapter = famRvAdapter
            binding.recFamRvPost.layoutManager = GridLayoutManager(requireActivity(),4, GridLayoutManager.HORIZONTAL, false)

            binding.latestRvPost.adapter = latestRvAdapter
            binding.latestRvPost.layoutManager = GridLayoutManager(requireActivity(), 4, GridLayoutManager.HORIZONTAL, false)


            viewModelUser.getNetworkUserResponseLiveData().observe(viewLifecycleOwner, Observer { response ->
                if (response != null && response.code == "0000"){
                    //Log.e("TEST USER INFO", response.userInfo.toString())
                    Glide.with(this)
                        .load(Secret.USER_MEDIA_URL+response.userInfo.profileImg)
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

            viewModelPost.getNetworkResponseLiveData().observe(viewLifecycleOwner, Observer { response ->
                if (response != null) {

                    Log.e("Test", response.size.toString())
                    //binding.user.text = GsonBuilder().setPrettyPrinting().create().toJson(response)
                    for(i:Int in 0 until response.size.toInt()){

                        latestRvitemList.add(ItemRecRV(response[i].title, response[i].nickname,response[i].coverImage))

                    }

                    latestRvAdapter.setItemClickListener(object: LatestRvAdapter.OnItemClickListener{
                        override fun onClick(v: View, position: Int) {

                            Log.e("TEST ITEM", response[position].toString())
                            var intent = Intent(context, PostActivity::class.java)
                            intent.putExtra("id",response[position].id)
                            startActivity(intent)

                        }
                    })

                }
            })

            viewModelPost.getPostFamousLiveData().observe(viewLifecycleOwner, Observer { response ->
                if (response != null) {
                    //Log.e("TEST",GsonBuilder().setPrettyPrinting().create().toJson(response))
                    Log.e("Test", response.size.toString())
                    //binding.user.text = GsonBuilder().setPrettyPrinting().create().toJson(response)
                    for(i:Int in 0 until response.size.toInt()){
                        famRVitemList.add(ItemFamRV(response[i].title, response[i].nickname,response[i].coverImage,response[i].views))
                    }

                    famRvAdapter.setItemClickListener(object: FamRvAdapter.OnItemClickListener{
                        override fun onClick(v: View, position: Int) {

                            Log.e("TEST ITEM", response[position].toString())
                            var intent = Intent(context, PostActivity::class.java)
                            intent.putExtra("id",response[position].id)
                            startActivity(intent)

                        }
                    })

                }

            })

            viewModelPost.getPostRecLiveData().observe(viewLifecycleOwner, Observer { response ->
                if (response != null) {

                    //Log.e("TEST",GsonBuilder().setPrettyPrinting().create().toJson(response))
                    Log.e("Test", response.size.toString())
                    //binding.user.text = GsonBuilder().setPrettyPrinting().create().toJson(response)
                    for(i:Int in 0 until response.size.toInt()){
                        recRvitemList.add(ItemRecRV(response[i].title, response[i].nickname,response[i].coverImage))
                    }

                    recRvAdapter.setItemClickListener(object: RecRVAdapter.OnItemClickListener{
                        override fun onClick(v: View, position: Int) {

                            Log.e("TEST ITEM", response[position].toString())
                            var intent = Intent(context, PostActivity::class.java)
                            intent.putExtra("id",response[position].id)
                            startActivity(intent)

                        }
                    })
                }

            })




            binding.layoutRefresh.isRefreshing = false
        }

        //추천 리사이클러뷰 자동 스크롤
        startAutoScroll(recRvAdapter, binding.recRvPost)

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

    private fun setFABClickEvent() {
        // 플로팅 버튼 클릭시 애니메이션 동작 기능
        binding.fabOption.setOnClickListener {
            toggleFab()
        }
        // 플로팅 버튼 클릭 이벤트 - write
        binding.fabWrite.setOnClickListener {

            isFabOpen = true
            toggleFab()

            val intent = Intent(context, WriteActivity::class.java)
            startActivity(intent)
        }

    }
    private fun toggleFab() {
        // 플로팅 액션 버튼 닫기 - 열려있는 플로팅 버튼 집어넣는 애니메이션
        if (isFabOpen) {
            binding.screenFab.setBackgroundColor(Color.argb(0, 0, 0, 0))
            ObjectAnimator.ofFloat(binding.fabWrite, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabOption, View.ROTATION, 45f, 0f).apply { start() }
        } else { // 플로팅 액션 버튼 열기 - 닫혀있는 플로팅 버튼 꺼내는 애니메이션
            binding.screenFab.setBackgroundColor(Color.parseColor("#4D000000"))
            ObjectAnimator.ofFloat(binding.fabWrite, "translationY", -180f).apply { start() }
            ObjectAnimator.ofFloat(binding.fabOption, View.ROTATION, 0f, 45f).apply { start() }
        }

        isFabOpen = !isFabOpen

    }

    private fun startAutoScroll(adapter:RecRVAdapter, recyclerView:RecyclerView) {
        var scrollPosition = 0
        val scrollDelay: Long = 3000 // 3초
        lifecycleScope.launch {
            while (true) {
                if (scrollPosition < adapter.itemCount) {
                    recyclerView.smoothScrollToPosition(scrollPosition)
                    scrollPosition++
                } else {
                    scrollPosition = 0
                }

                // 지정된 시간(3초) 대기
                delay(scrollDelay)
            }
        }
    }


}