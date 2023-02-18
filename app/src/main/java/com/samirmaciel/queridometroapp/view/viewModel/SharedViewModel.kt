package com.samirmaciel.queridometroapp.view.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samirmaciel.queridometroapp.model.FireBaseModels.Room
import com.samirmaciel.queridometroapp.model.FireBaseModels.RoomMember
import com.samirmaciel.queridometroapp.model.FireBaseModels.UserProfile

open class SharedViewModel : ViewModel() {

    var roomMemberList: MutableLiveData<MutableList<RoomMember>> = MutableLiveData()
    var roomEntered: MutableLiveData<Room> = MutableLiveData()
    val currentUserProfile: MutableLiveData<UserProfile> = MutableLiveData()

}