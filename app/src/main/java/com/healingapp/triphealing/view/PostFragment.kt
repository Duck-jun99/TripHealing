package com.healingapp.triphealing.view

import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.healingapp.triphealing.MainActivity
import com.healingapp.triphealing.PostActivity
import com.healingapp.triphealing.ProfileActivity
import com.healingapp.triphealing.R
import com.healingapp.triphealing.databinding.FragmentPostBinding
import com.healingapp.triphealing.datastore.DataStoreApplication
import com.healingapp.triphealing.model.post.NetworkDeleteResponse
import com.healingapp.triphealing.model.post.NetworkResponse
import com.healingapp.triphealing.model.user.UserInfoResponse
import com.healingapp.triphealing.network.post.ItemComment
import com.healingapp.triphealing.network.post_detail.PostDetailInterface
import com.healingapp.triphealing.network.user_info.UserInfoInterface
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.utils.ImageGetter
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelUser: UserViewModel

    val postDetailInterface by lazy { PostDetailInterface.create() }
    val userInfoInterface by lazy { UserInfoInterface.create() }

    var userName: String? = null

    private var flag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = (activity as PostActivity).id

        viewModelUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        CoroutineScope(Dispatchers.Main).launch {
            launch {
                userName = DataStoreApplication.getInstance().getDataStore().username.first()
                Log.e("PostFragment","DataStoreusername: ${userName}")
            }.join()

            launch {
                userName?.let { userInfoInterface.getNetwork(it).enqueue(object : Callback<UserInfoResponse> {
                    //서버 요청 성공
                    override fun onResponse(
                        call: Call<UserInfoResponse>,
                        response: Response<UserInfoResponse>
                    ) {
                        if(response.body()?.lovedPost?.contains(id) == true){
                            binding.layoutContainer.btnLoved.visibility = View.VISIBLE
                        }
                        else binding.layoutContainer.btnNotLoved.visibility = View.VISIBLE
                    }

                    override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                        //
                    }
                })
                }

                postDetailInterface.getNetwork(id)
                    .enqueue(object : Callback<NetworkResponse> {
                        //서버 요청 성공
                        override fun onResponse(
                            call: Call<NetworkResponse>,
                            response: Response<NetworkResponse>
                        ) {
                            Log.e("PostFragment", response.body().toString())

                            binding.tvPostTitle.text = response.body()?.title
                            binding.tvPostWriter.text =HtmlCompat.fromHtml("<b><i><font color=\"#808080\">by&nbsp;</font></i></b>${response.body()?.nickname}", HtmlCompat.FROM_HTML_MODE_LEGACY)

                            binding.layoutContainer.tvNickname.text = response.body()?.nickname
                            binding.layoutContainer.tvIntroduce.text = response.body()?.introduceText
                            binding.layoutContainer.tvCreatedDate.text = response.body()?.createdDate

                            //조회수
                            binding.layoutContainer.tvViews.text = "조회수: ${response.body()?.views}"

                            if(response.body()?.description != null){
                                displayHtml(response.body()!!.description, binding.layoutContainer.tvText)
                            }
                            else{
                                binding.layoutContainer.tvText.text = response.body()!!.text
                            }


                            //
                            if(response.body()?.profileImg != null){
                                Glide.with(requireActivity())
                                    .load(Secret.MEDIA_URL + response.body()!!.profileImg)
                                    .error(R.drawable.group_24)
                                    .into(binding.layoutContainer.imgProfile)
                            }
                            else{
                                Glide.with(requireActivity())
                                    .load(R.drawable.group_24)
                                    .error(R.drawable.group_24)
                                    .into(binding.layoutContainer.imgProfile)
                            }


                            if (response.body()?.coverImage != null) {
                                Glide.with(requireActivity())
                                    .load(Secret.MEDIA_URL + response.body()!!.coverImage)
                                    .error(R.drawable.group_24)
                                    .into(binding.imgPost)
                            }
                            else{
                                Glide.with(requireActivity())
                                    .load(R.drawable.group_24)
                                    .error(R.drawable.group_24)
                                    .into(binding.imgPost)
                            }

                            if(response.body()?.username == userName){
                                binding.layoutContainer.btnSetPost.visibility = View.VISIBLE
                            }

                            if (response.body()!!.comment.isNotEmpty()){

                                binding.layoutContainer.countComments.text = "댓글: ${response.body()!!.comment.size}개 보기"


                            }
                            else{
                                binding.layoutContainer.countComments.text = "아직 댓글이 없어요."
                            }

                            binding.layoutContainer.countComments.setOnClickListener {
                                val fragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_container_view_post)
                                if (fragment is PostFragment) {

                                    val fragment_comment = requireActivity().supportFragmentManager.beginTransaction()
                                    fragment_comment.replace(
                                        R.id.fragment_container_view_post,
                                        CommentFragment()
                                    )
                                    fragment_comment.commit()
                                }
                            }
                        }



                        override fun onFailure(call: Call<NetworkResponse>, t: Throwable) {
                            Log.e("POST DETAIL ERROR", "onFailure: error. cause: ${t.message}")

                        }
                    })
            }

        }

        binding.layoutContainer.btnNotLoved.setOnClickListener {

            userName?.let { it1 ->
                postDetailInterface.postLoved(id, it1,"true").enqueue(object : Callback<NetworkResponse> {
                    //서버 요청 성공
                    override fun onResponse(
                        call: Call<NetworkResponse>,
                        response: Response<NetworkResponse>
                    ) {
                        binding.layoutContainer.btnNotLoved.visibility = View.INVISIBLE
                        binding.layoutContainer.btnLoved.visibility = View.VISIBLE
                    }

                    override fun onFailure(call: Call<NetworkResponse>, t: Throwable) {
                        //
                    }
                })
            }
        }

        binding.layoutContainer.btnLoved.setOnClickListener {

            userName?.let { it1 ->
                postDetailInterface.postLoved(id, it1,"false").enqueue(object : Callback<NetworkResponse> {
                    //서버 요청 성공
                    override fun onResponse(
                        call: Call<NetworkResponse>,
                        response: Response<NetworkResponse>
                    ) {
                        binding.layoutContainer.btnLoved.visibility = View.INVISIBLE
                        binding.layoutContainer.btnNotLoved.visibility = View.VISIBLE
                    }

                    override fun onFailure(call: Call<NetworkResponse>, t: Throwable) {
                        //
                    }
                })
            }
        }


        binding.layoutContainer.btnSetPost.setOnClickListener {
            userName?.let { it1 -> showPopupMenu(binding.layoutContainer.btnSetPost, id, it1) }
        }


        binding.imgPost.scaleType = ImageView.ScaleType.CENTER_CROP

        binding.layoutContainer.imgProfile.setOnClickListener {
            val intent = Intent(requireActivity(), ProfileActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun displayHtml(html: String, html_viewer:TextView) {

        // Creating object of ImageGetter class you just created
        val imageGetter = ImageGetter(resources, html_viewer)

        // Using Html framework to parse html
        val styledText=HtmlCompat.fromHtml(html,
            HtmlCompat.FROM_HTML_MODE_LEGACY,
            imageGetter,null)

        // setting the text after formatting html and downloading and setting images
        html_viewer.text = styledText

        // to enable image/link clicking
        html_viewer.movementMethod = LinkMovementMethod.getInstance()

    }


    private fun showPopupMenu(view: View, id:String, userName:String) {
        // PopupMenu 인스턴스 생성
        val popupMenu = PopupMenu(requireContext(), view)

        // 메뉴 리소스를 팽창하여 PopupMenu에 추가
        popupMenu.menuInflater.inflate(R.menu.menu_post, popupMenu.menu)

        // PopupMenu 이벤트 리스너 설정
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    Toast.makeText(requireContext(), "삭제 메뉴 클릭", Toast.LENGTH_SHORT).show()
                    postDetailInterface.deletePost(id,userName)
                        .enqueue(object : Callback<NetworkDeleteResponse> {
                            //서버 요청 성공
                            override fun onResponse(
                                call: Call<NetworkDeleteResponse>,
                                response: Response<NetworkDeleteResponse>
                            ) {
                                if(response.body()?.code == "0000"){
                                    Toast.makeText(requireContext(), "삭제 완료했어요!", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(context, MainActivity::class.java)
                                    startActivity(intent)
                                    requireActivity().finish()
                                }
                                else Toast.makeText(requireContext(), "삭제 실패!", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<NetworkDeleteResponse>, t: Throwable) {
                                //TODO("Not yet implemented")
                            }
                        })
                    true
                }
                R.id.update -> {
                    Toast.makeText(requireContext(), "업데이트 메뉴 클릭", Toast.LENGTH_SHORT).show()

                    true
                }
                else -> false
            }
        }

        // PopupMenu 표시
        popupMenu.show()
    }
}
