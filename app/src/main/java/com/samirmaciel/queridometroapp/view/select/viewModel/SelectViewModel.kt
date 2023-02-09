package com.samirmaciel.queridometroapp.view.select.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samirmaciel.queridometroapp.mock.UserMock
import com.samirmaciel.queridometroapp.model.EmojiCarouselItem
import com.samirmaciel.queridometroapp.model.UserProfileItem
import com.samirmaciel.queridometroapp.model.UserProfileItemSelection

class SelectViewModel : ViewModel() {

    var mUserProfileItemList: MutableLiveData<List<UserProfileItem>>? = null

    fun initializeViewModel(userProfileItemList: MutableLiveData<List<UserProfileItem>>?){
        mUserProfileItemList = userProfileItemList
    }

    fun validateSelectionUsersEmoji(list: List<UserProfileItemSelection>): Boolean{
        var result = true
        list.forEach {
            if(it.userEmojiSelected == null) result = false
        }

        return result
    }

    fun generatedUserProfileItemList(userProfileItemSelection: List<UserProfileItemSelection>){
        UserMock.generalUserList = userProfileItemSelection.map { userProfileItemSelection ->
            val emojiList = verifyHasEmoji(userProfileItemSelection.userProfileItem.emojiList, userProfileItemSelection.userEmojiSelected)
            UserProfileItem(userProfileItemSelection.userProfileItem.userName, userProfileItemSelection.userProfileItem.profileImage, emojiList) }
    }

    private fun verifyHasEmoji(emojiList: MutableList<EmojiCarouselItem>, emojiID: Int?): MutableList<EmojiCarouselItem>{
        var hasEmoji = false

        emojiList.forEach {
            if(it.emojiID == emojiID){
                hasEmoji = true
                it.count += 1
            }
        }

        if(!hasEmoji && emojiID != null){
            emojiList.add(EmojiCarouselItem(emojiID, 1))
            return emojiList
        }

        return emojiList
    }
}