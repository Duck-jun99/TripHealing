package com.healingapp.triphealing.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.healingapp.triphealing.PostActivity
import com.healingapp.triphealing.R
import com.healingapp.triphealing.databinding.FragmentCommentBinding
import com.healingapp.triphealing.databinding.FragmentPostBinding
import com.healingapp.triphealing.datastore.DataStoreApplication
import com.healingapp.triphealing.model.post.NetworkCommentResponse
import com.healingapp.triphealing.model.post.NetworkDeleteResponse
import com.healingapp.triphealing.model.post.NetworkResponse
import com.healingapp.triphealing.network.post.ItemComment
import com.healingapp.triphealing.network.post_detail.PostDetailInterface
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentFragment:Fragment() {

    private var _binding: FragmentCommentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelUser: UserViewModel

    val postDetailInterface by lazy { PostDetailInterface.create() }

    var userName: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = (activity as PostActivity).id
        var nickName:String? = null
        var body:String? = null
        var profileImg:String? =null

        viewModelUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        viewModelUser.getNetworkUserResponseLiveData().observe(viewLifecycleOwner, Observer{response ->
            if (response != null){

                //로그인 되어 있을 시,
                if(response.code=="0000"){
                    Glide.with(this)
                        .load(Secret.USER_MEDIA_URL+response.userInfo.profileImg)
                        .error(R.drawable.group_24)
                        .circleCrop()
                        .into(binding.imgComment)
                    binding.btnComment.isVisible = true
                    nickName = response.userInfo.nickname
                    profileImg = response.userInfo.profileImg
                }
                else{
                    Glide.with(this)
                        .load(R.drawable.group_24)
                        .error(R.drawable.group_24)
                        .circleCrop()
                        .into(binding.imgComment)
                    binding.etComment.hint = "로그인해주세요."
                    binding.btnComment.isVisible = false
                }
            }
        })

        CoroutineScope(Dispatchers.Default).launch {
            launch {
                userName = DataStoreApplication.getInstance().getDataStore().username.first()
                Log.e("PostFragment","username: ${userName}")
            }.join()

            launch {
                postDetailInterface.getNetwork(id)
                    .enqueue(object : Callback<NetworkResponse> {
                        //서버 요청 성공
                        override fun onResponse(
                            call: Call<NetworkResponse>,
                            response: Response<NetworkResponse>) {

                            if (response.body()!!.comment.isNotEmpty()){

                                //val commentData:ArrayList<ItemComment> = ArrayList()

                                for(i in 0 until response.body()!!.comment.size){

                                    //Log.e("TEST COMMENT DATA", "${nickName}, ${comment}, ${date}, ${img}")
                                    //commentData.add(ItemComment(response.body()!!.comment[i].writer,response.body()!!.comment[i].body,response.body()!!.comment[i].date,response.body()!!.comment[i].profileImg))
                                    //binding.layoutComment.addView(createLayout(commentData))
                                    binding.layoutComment.addView(createLayout(response.body()!!.comment[i].writer,response.body()!!.comment[i].body,response.body()!!.comment[i].date,response.body()!!.comment[i].profileImg))
                                }

                            }

                        }
                        override fun onFailure(call: Call<NetworkResponse>, t: Throwable) {
                            Log.e("POST DETAIL ERROR", "onFailure: error. cause: ${t.message}")

                        }
                    })
            }


        }

        binding.btnComment.setOnClickListener {
            body = binding.etComment.text.toString()
            CoroutineScope(Dispatchers.Default).launch {
                nickName?.let {
                    postDetailInterface.postComment(id, nickName = it, body = body!!)
                        .enqueue(object : Callback<NetworkCommentResponse> {
                            //서버 요청 성공
                            override fun onResponse(
                                call: Call<NetworkCommentResponse>,
                                response: Response<NetworkCommentResponse>) {
                                Log.e("CommentFragment", response.body().toString())
                                binding.layoutComment.addView(profileImg?.let { it1 -> createLayout(writer = response.body()!!.writer, body = response.body()!!.body, date = response.body()!!.date, profileImg = it1) })
                            }

                            override fun onFailure(call: Call<NetworkCommentResponse>, t: Throwable) {
                                Log.e("POST DETAIL ERROR", "onFailure: error. cause: ${t.message}")

                            }
                        })
                }
            }
        }
    }

    private fun createLayout(writer:String, body:String, date:String, profileImg:String) :View{

        val inflater = this.layoutInflater
        val layout = inflater.inflate(R.layout.fragment_post3, null) as LinearLayout

        val tvNickName = layout.findViewById<TextView>(R.id.tv_nickname)
        val tvComment = layout.findViewById<TextView>(R.id.tv_comments)
        val imgProfile = layout.findViewById<ImageView>(R.id.img_profile)
        val tvTime = layout.findViewById<TextView>(R.id.tv_time)

        tvNickName.text = writer
        tvComment.text = body
        tvTime.text = date

        Glide.with(this)
            .load(Secret.MEDIA_URL+profileImg)
            .error(R.drawable.group_24)
            .into(imgProfile)




        return layout
    }
}