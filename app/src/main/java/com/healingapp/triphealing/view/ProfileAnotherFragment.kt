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
import com.healingapp.triphealing.ProfileAnotherActivity
import com.healingapp.triphealing.R
import com.healingapp.triphealing.adapter.FamRvAdapter
import com.healingapp.triphealing.databinding.FragmentProfileAnotherBinding
import com.healingapp.triphealing.databinding.FragmentProfileBinding
import com.healingapp.triphealing.datastore.DataStoreApplication
import com.healingapp.triphealing.model.post.NetworkResponse
import com.healingapp.triphealing.model.update.NetworkUpdateResponse
import com.healingapp.triphealing.model.user.NetworkUserResponse
import com.healingapp.triphealing.model.user.UserInfoResponse
import com.healingapp.triphealing.network.login.UserPostInterface
import com.healingapp.triphealing.network.post.ItemFamRV
import com.healingapp.triphealing.network.update.UpdateInterface
import com.healingapp.triphealing.network.user_info.UserInfoInterface
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.viewmodel.post_all.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileAnotherFragment : Fragment() {

    private var _binding: FragmentProfileAnotherBinding? = null
    private val binding get() = _binding!!

    val userPostInterface by lazy { UserPostInterface.create() }
    val userInfoInterface by lazy { UserInfoInterface.create() }

    lateinit var userMeName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileAnotherBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userName = (activity as ProfileAnotherActivity).userName

        CoroutineScope(Dispatchers.Default).launch {
            userMeName = DataStoreApplication.getInstance().getDataStore().username.first()
        }

        userInfoInterface.getNetwork(userName).enqueue(object :
            Callback<UserInfoResponse>{
            override fun onResponse(
                call: Call<UserInfoResponse>,
                response: Response<UserInfoResponse>
            ) {
                if(response.body()!=null){
                    Log.e("ProfileAnotherFragment",response.body().toString())
                    Glide.with(requireContext())
                        .load(Secret.USER_MEDIA_URL+ response.body()!!.profileImg)
                        .error(R.drawable.group_24)
                        .circleCrop()
                        .into(binding.imgProfile)

                    Glide.with(requireContext())
                        .load(Secret.USER_MEDIA_URL+response.body()!!.backgroundImg)
                        .centerCrop() // 이미지를 가득 채우기 위해 잘라내기
                        //.placeholder(ColorDrawable(Color.BLACK))
                        .error(R.drawable.background2)
                        .into(binding.imgProfileBack)

                    binding.tvNickname.text = response.body()!!.nickname
                    binding.tvIntroduce.text = response.body()!!.introduceText

                    binding.tvFollower.text = "팔로우: ${response.body()!!.follower.size.toString()}명"
                    binding.tvFollowing.text = "팔로잉: ${response.body()!!.following.size.toString()}명"

                    //여행성향
                    binding.tvPropensity.text = HtmlCompat.fromHtml(
                        "<p><span style=\"background-color:#FFE6E6\"> ${response.body()!!.propensity.mbti} </span> &nbsp;" +
                                "<span style=\"background-color:#E6E6FA\"> ${response.body()!!.propensity.option1} </span> &nbsp; " +
                                "<span style=\"background-color:#E1F8FF\"> ${response.body()!!.propensity.option2} </span> &nbsp; " +
                                "<span style=\"background-color:#FFFFF0\"> ${response.body()!!.propensity.option3} </span> &nbsp; " +
                                "<span style=\"background-color:#DCFFE4\"> ${response.body()!!.propensity.option4} </span><p>\n"
                        ,HtmlCompat.FROM_HTML_MODE_LEGACY)

                    if(userMeName in response.body()!!.follower){
                        binding.btnFollow.visibility = View.INVISIBLE
                        binding.btnUnFollow.visibility = View.VISIBLE
                    }

                    else{
                        binding.btnFollow.visibility = View.VISIBLE
                        binding.btnUnFollow.visibility = View.INVISIBLE
                    }

                }

            }

            override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                Log.e("ProfileAnotherFragment",t.message.toString())
            }

        })


        binding.btnFollow.setOnClickListener {
            userInfoInterface.postFollow(userName=userName, userMeName = userMeName, "follow").enqueue(object : Callback<UserInfoResponse>{
                override fun onResponse(
                    call: Call<UserInfoResponse>,
                    response: Response<UserInfoResponse>
                ) {
                    if (response.body()!= null){
                        binding.btnFollow.visibility = View.INVISIBLE
                        binding.btnUnFollow.visibility = View.VISIBLE

                        binding.tvFollower.text = "팔로우: ${response.body()!!.follower.size.toString()}명"
                    }
                }

                override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                    //TODO("Not yet implemented")
                }

            })
        }

        binding.btnUnFollow.setOnClickListener {
            userInfoInterface.postFollow(userName=userName, userMeName = userMeName, "unfollow").enqueue(object : Callback<UserInfoResponse>{
                override fun onResponse(
                    call: Call<UserInfoResponse>,
                    response: Response<UserInfoResponse>
                ) {
                    if(response.body()!=null){
                        binding.btnFollow.visibility = View.VISIBLE
                        binding.btnUnFollow.visibility = View.INVISIBLE

                        binding.tvFollower.text = "팔로우: ${response.body()!!.follower.size.toString()}명"
                    }

                }

                override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                    //TODO("Not yet implemented")
                }

            })
        }


        val recUserPostList = ArrayList<ItemFamRV>()
        val userRvAdapter = FamRvAdapter(recUserPostList)

        binding.recUser.adapter = userRvAdapter
        binding.recUser.layoutManager = GridLayoutManager(requireActivity(),4, GridLayoutManager.HORIZONTAL, false)

        userPostInterface.getNetwork(userName).enqueue(object :
            Callback<List<NetworkResponse>>{
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



    }

}