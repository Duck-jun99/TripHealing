package com.healingapp.triphealing.viewmodel.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.healingapp.triphealing.model.user.NetworkUserResponse
import com.healingapp.triphealing.network.login.UserRepository
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private var UsergetRepository: UserRepository
    private var UserResponseLiveData: LiveData<NetworkUserResponse>

    init {
        UsergetRepository = UserRepository()
        UserResponseLiveData = UsergetRepository.getNetworkUserResponseLiveData()
    }

    fun getNetwork(username:String, password:String) {
        viewModelScope.launch {
            UsergetRepository.getNetwork(username, password)
        }

    }

    fun getNetworkUserResponseLiveData(): LiveData<NetworkUserResponse> {
        return UserResponseLiveData
    }
}