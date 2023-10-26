package com.healingapp.triphealing.viewmodel.post_all

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.healingapp.triphealing.model.post.NetworkRecWriterResponse
import com.healingapp.triphealing.model.post.NetworkResponse
import com.healingapp.triphealing.network.post.NetworkRepository
import kotlinx.coroutines.launch

class NetworkViewModel(application: Application) : AndroidViewModel(application) {

    private var NetworkgetRepository: NetworkRepository
    //post all
    private var NetworkResponseLiveData: LiveData<List<NetworkResponse>>

    //post famous
    private var PostFamousLiveData: LiveData<List<NetworkResponse>>

    //post recommendation
    private var PostRecLiveData: LiveData<List<NetworkResponse>>

    //post Recommended writer
    private var PostRecWriterLiveData: LiveData<List<NetworkRecWriterResponse>>

    init {
        NetworkgetRepository = NetworkRepository()
        NetworkResponseLiveData = NetworkgetRepository.getNetworkResponseLiveData()
        PostFamousLiveData = NetworkgetRepository.getPostFamousLiveData()
        PostRecLiveData = NetworkgetRepository.getPostRecLiveData()
        PostRecWriterLiveData = NetworkgetRepository.getPostRecWriterLiveData()
    }

    //view에서 호출시, post all, post famous, post recommendation, post Recommended writer 다 호출
    fun getNetwork() {
        viewModelScope.launch {
            NetworkgetRepository.getNetwork()
        }

    }

    fun getNetworkResponseLiveData(): LiveData<List<NetworkResponse>> {
        return NetworkResponseLiveData
    }

    fun getPostFamousLiveData(): LiveData<List<NetworkResponse>> {
        return PostFamousLiveData
    }

    fun getPostRecLiveData(): LiveData<List<NetworkResponse>> {
        return PostRecLiveData
    }

    fun getPostRecWriterLiveData(): LiveData<List<NetworkRecWriterResponse>> {
        return PostRecWriterLiveData
    }
}