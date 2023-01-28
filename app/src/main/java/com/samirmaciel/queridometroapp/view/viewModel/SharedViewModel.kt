package com.samirmaciel.queridometroapp.view.viewModel

import androidx.constraintlayout.utils.widget.MockView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samirmaciel.queridometroapp.mock.UserMock
import com.samirmaciel.queridometroapp.model.UserProfileItem

class SharedViewModel : ViewModel() {

    var userProfileItemList: MutableLiveData<List<UserProfileItem>> = MutableLiveData()

    init {
        loadUserProfileItemList()
    }

    private fun loadUserProfileItemList(){
        userProfileItemList.value = UserMock.getUserCarouselListItem()
    }
}