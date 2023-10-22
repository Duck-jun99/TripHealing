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
import android.widget.Toast
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

        requestPermission()

        //viewModelPost = ViewModelProvider(requireActivity())[NetworkViewModel::class.java]
        //viewModelUser = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        lateinit var token:String



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

    private fun requestPermission() {
        val locationResultLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (!it) {
                Toast.makeText(context, "스토리지에 접근 권한을 허가해주세요", Toast.LENGTH_SHORT).show()
            }
        }
        locationResultLauncher.launch(
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }
}