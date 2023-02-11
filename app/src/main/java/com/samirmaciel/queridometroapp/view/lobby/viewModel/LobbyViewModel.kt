package com.samirmaciel.queridometroapp.view.lobby.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samirmaciel.queridometroapp.view.viewModel.SharedViewModel

class LobbyViewModel: ViewModel() {

    private var mSharedViewModel: SharedViewModel? = null
    val roomIDSelected: MutableLiveData<String?> = MutableLiveData()

    fun initializeViewModel(sharedViewModel: SharedViewModel){
        mSharedViewModel = sharedViewModel
    }

    fun validateRoomID(roomID: String?): Boolean{

        return true
    }
}