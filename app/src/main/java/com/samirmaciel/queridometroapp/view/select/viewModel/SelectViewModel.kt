package com.samirmaciel.queridometroapp.view.select.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samirmaciel.queridometroapp.model.Emoji
import com.samirmaciel.queridometroapp.model.FireBaseModels.UserProfile
import com.samirmaciel.queridometroapp.model.UserProfileItemSelection

class SelectViewModel : ViewModel() {

    var mUserListProfile: MutableLiveData<List<UserProfile>>? = null

    fun initializeViewModel(userProfileList: MutableLiveData<List<UserProfile>>?){
        mUserListProfile = userProfileList
    }

    fun validateSelectionUsersEmoji(list: List<UserProfileItemSelection>): Boolean{
        var result = true
        list.forEach {
            if(it.userEmojiSelected == null) result = false
        }

        return result
    }

    fun generatedUserProfileItemList(userProfileItemSelection: List<UserProfileItemSelection>){
        mUserListProfile?.value = userProfileItemSelection.map { userProfileItemSelection ->
            val emojiList = verifyHasEmoji(userProfileItemSelection.userProfile.currentEmojiList, userProfileItemSelection.userEmojiSelected)
            UserProfile(userProfileItemSelection.userProfile.userName, "xxxxxx", profileImage = userProfileItemSelection.userProfile.profileImage, currentEmojiList = emojiList) }
    }

    private fun verifyHasEmoji(emojiList: MutableList<Emoji>, emojiID: Int?): MutableList<Emoji>{
        var hasEmoji = false

        emojiList.forEach {
            if(it.emojiID == emojiID){
                hasEmoji = true
                it.count += 1
            }
        }

        if(!hasEmoji && emojiID != null){
            emojiList.add(Emoji(emojiID, 1))
            return emojiList
        }

        return emojiList
    }
}