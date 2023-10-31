package com.healingapp.triphealing.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.healingapp.triphealing.PostActivity
import com.healingapp.triphealing.ProfileActivity
import com.healingapp.triphealing.R
import com.healingapp.triphealing.databinding.FragmentPostBinding
import com.healingapp.triphealing.model.post.NetworkResponse
import com.healingapp.triphealing.network.post.ItemComment
import com.healingapp.triphealing.network.post_detail.PostDetailInterface
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.utils.ImageGetter
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelUser: UserViewModel

    private val scope = CoroutineScope(Dispatchers.Default)

    val postDetailInterface by lazy { PostDetailInterface.create() }

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


        scope.launch {
            //val token = DataStoreApplication.getInstance().getDataStore().text.first()
            postDetailInterface.getNetwork(id)
                .enqueue(object : Callback<NetworkResponse> {
                    //서버 요청 성공
                    override fun onResponse(
                        call: Call<NetworkResponse>,
                        response: Response<NetworkResponse>
                    ) {
                        Log.e("TEST ALL", response.body().toString())
                        Log.e("TEST comment", response.body()?.comment.toString())

                        binding.tvPostTitle.text = response.body()?.title
                        binding.tvPostWriter.text =HtmlCompat.fromHtml("<b><i><font color=\"#808080\">by&nbsp;</font></i></b>${response.body()?.nickname}", HtmlCompat.FROM_HTML_MODE_LEGACY)

                        //binding.layoutContainer.tvText.text = response.body()?.text
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

                        if (response.body()!!.comment.isNotEmpty()){

                            binding.layoutContainer.countComments.text = "댓글: ${response.body()!!.comment.size}개"

                            val commentData:ArrayList<ItemComment> = ArrayList()

                            for(i in 0 until response.body()!!.comment.size){

                                //Log.e("TEST COMMENT DATA", "${nickName}, ${comment}, ${date}, ${img}")
                                commentData.add(ItemComment(response.body()!!.comment[i].writer,response.body()!!.comment[i].body,response.body()!!.comment[i].date,response.body()!!.comment[i].profileImg))

                            }

                            binding.layoutContainer.countComments.setOnClickListener {

                                if(flag ==0){
                                    binding.layoutContainer.layoutCommentsContainer.isVisible = true
                                    binding.layoutContainer.layoutCommentsContainer.addView(createLayout(commentData))

                                    flag += 1
                                }
                                else if(flag ==1){
                                    binding.layoutContainer.layoutCommentsContainer.isVisible = false
                                    binding.layoutContainer.layoutCommentsContainer.removeView(getView())
                                    flag -= 1
                                }

                            }



                        }
                        else{
                            binding.layoutContainer.countComments.text = "아직 댓글이 없어요."
                        }
                    }



                    override fun onFailure(call: Call<NetworkResponse>, t: Throwable) {
                        Log.e("POST DETAIL ERROR", "onFailure: error. cause: ${t.message}")

                    }
                })
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

    private fun createLayout(commentData:ArrayList<ItemComment>) :View{

        val inflater = this.layoutInflater
        val layout = inflater.inflate(R.layout.fragment_post3, null) as LinearLayout

        val tvNickName = layout.findViewById<TextView>(R.id.tv_nickname)
        val tvComment = layout.findViewById<TextView>(R.id.tv_comments)
        val imgProfile = layout.findViewById<ImageView>(R.id.img_profile)
        val tvTime = layout.findViewById<TextView>(R.id.tv_time)

        for(i in 0 until commentData.size){
            tvNickName.text = commentData[i].nickName
            tvComment.text = commentData[i].comment
            tvTime.text = commentData[i].date

            Glide.with(this)
                .load(Secret.MEDIA_URL+commentData[i].img)
                .error(R.drawable.group_24)
                .into(imgProfile)
        }




        return layout
    }

}