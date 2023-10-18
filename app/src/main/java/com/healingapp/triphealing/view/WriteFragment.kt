package com.healingapp.triphealing.view

import android.R.attr.data
import android.R.attr.type
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.dataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.healingapp.triphealing.databinding.FragmentWriteBinding
import com.healingapp.triphealing.datastore.DataStoreApplication
import com.healingapp.triphealing.network.write.WriteInterface
import com.healingapp.triphealing.secret.Secret
import com.healingapp.triphealing.utils.MyJavaScriptInterface
import com.healingapp.triphealing.viewmodel.post_all.NetworkViewModel
import com.healingapp.triphealing.viewmodel.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date


class WriteFragment : Fragment() {

    private var _binding: FragmentWriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModelPost: NetworkViewModel
    private lateinit var viewModelUser: UserViewModel

    private val scope = CoroutineScope(Dispatchers.Default)

    val writeInterface by lazy { WriteInterface.create() }

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWriteBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //viewModelPost = ViewModelProvider(requireActivity())[NetworkViewModel::class.java]
        //viewModelUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        lateinit var token:String

        var cameraPath = ""
        var mWebViewImageUpload: ValueCallback<Array<Uri>>? = null

        webView = binding.webView
        val settings = webView.settings
        settings.javaScriptEnabled = true


        webView.webChromeClient = WebChromeClient()

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // 페이지 내에서 리디렉션을 처리하기 위한 설정
                view.loadUrl(url)

                return true
            }
        }
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: WebChromeClient.FileChooserParams?): Boolean {
                try{
                    mWebViewImageUpload = filePathCallback!!
                    var takePictureIntent : Intent?
                    takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if(takePictureIntent.resolveActivity(requireActivity().packageManager) != null){
                        var photoFile : File?

                        photoFile = createImageFile()
                        takePictureIntent.putExtra("PhotoPath",cameraPath)

                        if(photoFile != null){
                            cameraPath = "file:${photoFile.absolutePath}"
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photoFile))
                        }
                        else takePictureIntent = null
                    }
                    val contentSelectionIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    contentSelectionIntent.type = "image/*"

                    var intentArray: Array<Intent?>

                    if(takePictureIntent != null) intentArray = arrayOf(takePictureIntent)
                    else intentArray = takePictureIntent?.get(0)!!

                    val chooserIntent = Intent(Intent.ACTION_CHOOSER)
                    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent)
                    chooserIntent.putExtra(Intent.EXTRA_TITLE,"사용할 앱을 선택해주세요.")
                    chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
                    launcher.launch(chooserIntent)
                }
                catch (e : Exception){ }
                return true
            }

            fun createImageFile(): File? {
                @SuppressLint("SimpleDateFormat")
                val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val imageFileName = "img_" + timeStamp + "_"
                val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                return File.createTempFile(imageFileName, ".jpg", storageDir)
            }

            val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intent = result.data
                    Log.e("IMAGE DATA",result.toString())

                    if(intent == null){ //바로 사진을 찍어서 올리는 경우
                        val results = arrayOf(Uri.parse(cameraPath))
                        mWebViewImageUpload!!.onReceiveValue(results!!)
                    }
                    else{ //사진 앱을 통해 사진을 가져온 경우
                        val results = intent!!.data!!
                        mWebViewImageUpload!!.onReceiveValue(arrayOf(results!!))
                    }
                }
                else{ //취소 한 경우 초기화
                    mWebViewImageUpload!!.onReceiveValue(null)
                    mWebViewImageUpload = null
                }
            }
        }

        // CKEditor가 호스팅된 웹 페이지의 URL을 설정.
        webView.loadUrl(Secret.WRITE_URL)


        lifecycleScope.launch {
            token = DataStoreApplication.getInstance().getDataStore().text.first()
        }


        val myJavaScriptInterface = MyJavaScriptInterface(requireActivity(),token)


        webView.addJavascriptInterface(myJavaScriptInterface, "Android")



        binding.btnBackWrite.setOnClickListener {
            requireActivity().finish()
        }

        binding.btnPostWrite.setOnClickListener {


        }


    }
}