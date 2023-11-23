package com.healingapp.triphealing.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.healingapp.triphealing.PostActivity
import com.healingapp.triphealing.R
import com.healingapp.triphealing.adapter.FamRvAdapter
import com.healingapp.triphealing.databinding.FragmentProfileBinding
import com.healingapp.triphealing.model.post.NetworkResponse
import com.healingapp.triphealing.model.update.NetworkUpdateResponse
import com.healingapp.triphealing.model.user.NetworkUserResponse
import com.healingapp.triphealing.network.login.UserPostInterface
import com.healingapp.triphealing.network.post.ItemFamRV
import com.healingapp.triphealing.network.update.UpdateInterface
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.viewmodel.post_all.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel

    val userPostInterface by lazy { UserPostInterface.create() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recUserPostList = ArrayList<ItemFamRV>()
        val userRvAdapter = FamRvAdapter(recUserPostList)

        binding.recUser.adapter = userRvAdapter
        binding.recUser.layoutManager = GridLayoutManager(requireActivity(),4, GridLayoutManager.HORIZONTAL, false)


        // ProfileActivity에서 설정한 ViewModel 가져오기
        //viewModelPost = ViewModelProvider(requireActivity())[NetworkViewModel::class.java]
        viewModelUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        //viewModelUser = ViewModelProvider(this)[UserViewModel::class.java]

        viewModelUser.getNetworkUserResponseLiveData().observe(viewLifecycleOwner, Observer { response ->
            if (response != null){
                Log.e("TEST RESPONSE", response.toString())

                //로그인 되어 있을 시,
                if(response.code=="0000"){


                    Glide.with(this)
                        .load(Secret.USER_MEDIA_URL+response.userInfo.profileImg)
                        .error(R.drawable.group_24)
                        .circleCrop()
                        .into(binding.imgProfile)

                    Glide.with(this)
                        .load(Secret.USER_MEDIA_URL+response.userInfo.backgroundImg)
                        .centerCrop() // 이미지를 가득 채우기 위해 잘라내기
                        //.placeholder(ColorDrawable(Color.BLACK))
                        .error(R.drawable.background2)
                        .into(binding.imgProfileBack)

                    binding.tvNickname.text = response.userInfo.nickname
                    binding.tvIntroduce.text = response.userInfo.introduceText

                    binding.tvFollower.text = "팔로우: ${response.userInfo.follower.size.toString()}명"
                    binding.tvFollowing.text = "팔로잉: ${response.userInfo.following.size.toString()}명"

                    //여행성향
                    binding.tvPropensity.text = HtmlCompat.fromHtml(
                        "<p><span style=\"background-color:#FFE6E6\"> ${response.userInfo.propensity.mbti} </span> &nbsp;" +
                                "<span style=\"background-color:#E6E6FA\"> ${response.userInfo.propensity.option1} </span> &nbsp; " +
                                "<span style=\"background-color:#E1F8FF\"> ${response.userInfo.propensity.option2} </span> &nbsp; " +
                                "<span style=\"background-color:#FFFFF0\"> ${response.userInfo.propensity.option3} </span> &nbsp; " +
                                "<span style=\"background-color:#DCFFE4\"> ${response.userInfo.propensity.option4} </span><p>\n"
                        ,HtmlCompat.FROM_HTML_MODE_LEGACY)

                    userPostInterface.getNetwork(response.userInfo.username).enqueue(object :
                        Callback<List<NetworkResponse>> {
                        //서버 요청 성공
                        override fun onResponse(
                            call: Call<List<NetworkResponse>>,
                            response: Response<List<NetworkResponse>>
                        ) {
                            if(response.body() != null){
                                for(i:Int in 0 until response.body()!!.size.toInt()){
                                    recUserPostList.add(ItemFamRV(response.body()!![i].title, response.body()!![i].nickname,response.body()!![i].coverImage,response.body()!![i].views))
                                }
                                userRvAdapter.notifyDataSetChanged()

                                userRvAdapter.setItemClickListener(object: FamRvAdapter.OnItemClickListener{
                                    override fun onClick(v: View, position: Int) {

                                        Log.e("TEST ITEM", response.body()!![position].toString())
                                        var intent = Intent(context, PostActivity::class.java)
                                        intent.putExtra("id",response.body()!![position].id)
                                        startActivity(intent)

                                    }
                                })
                            }


                        }

                        override fun onFailure(call: Call<List<NetworkResponse>>, t: Throwable) {
                            //TODO("Not yet implemented")
                        }
                    })

                    //여행성향 예시
                    /*
                    binding.tvPropensity.text =
                        "${response.userInfo.propensity.mbti} "+
                        "${response.userInfo.propensity.option1} " +
                            " ${response.userInfo.propensity.option2} " +
                            "${response.userInfo.propensity.option3} " +
                            "${response.userInfo.propensity.option4}"
                    */
                    //HtmlCompat.fromHtml("<b><i><font color=\"#808080\">by&nbsp;</font></i></b>${response.body()?.nickname}", HtmlCompat.FROM_HTML_MODE_LEGACY)

                    //<p><span style="background-color:#FFE6E6"> Git </span>  <span style="background-color:#E6E6FA"> Github </span>  <span style="background-color:#E1F8FF"> AndroidStudio </span>  <span style="background-color:#FFFFF0"> VisualStudioCode </span>  <span style="background-color:#F5F5F5"> PhotoShop </span>  <span style="background-color:#DCFFE4"> Vim </span><p>
                }

                //로그인 되어 있지 않을 때
                else{
                    Glide.with(this)
                        .load(R.drawable.group_24)
                        .error(R.drawable.group_24)
                        .circleCrop()
                        .into(binding.imgProfile)

                    Glide.with(this)
                        .load(R.drawable.background2)
                        .error(R.drawable.background2)
                        .into(binding.imgProfileBack)

                    binding.tvNickname.text = "로그인해주세요"
                    binding.tvIntroduce.text = "로그인해주세요"

                    binding.tvFollower.isVisible = false
                    binding.tvFollowing.isVisible = false
                    binding.tvPropensity.isVisible = false

                }
            }
        })


    }

}