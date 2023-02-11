package com.samirmaciel.queridometroapp.view.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samirmaciel.queridometroapp.mock.UserMock
import com.samirmaciel.queridometroapp.model.FireBaseModels.UserProfile

open class SharedViewModel : ViewModel() {

    var userProfileList: MutableLiveData<List<UserProfile>> = MutableLiveData()

    init {

    }

    private fun loadUserProfileItemList(){
        userProfileList.value = UserMock.getUserCarouselListItem()
    }
}