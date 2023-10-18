package com.healingapp.triphealing.viewmodel.post_all

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.healingapp.triphealing.model.post.NetworkResponse
import com.healingapp.triphealing.network.post_all.NetworkRepository
import kotlinx.coroutines.launch

class NetworkViewModel(application: Application) : AndroidViewModel(application) {
    private var NetworkgetRepository: NetworkRepository
    private var NetworkResponseLiveData: LiveData<List<NetworkResponse>>

    init {
        NetworkgetRepository = NetworkRepository()
        NetworkResponseLiveData = NetworkgetRepository.getNetworkResponseLiveData()
    }

    fun getNetwork() {
        viewModelScope.launch {
            NetworkgetRepository.getNetwork()
        }

    }

    fun getNetworkResponseLiveData(): LiveData<List<NetworkResponse>> {
        return NetworkResponseLiveData
    }
}