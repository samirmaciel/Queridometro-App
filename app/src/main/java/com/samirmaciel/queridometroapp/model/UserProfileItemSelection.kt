package com.samirmaciel.queridometroapp.model

import com.samirmaciel.queridometroapp.model.FireBaseModels.UserProfile

data class UserProfileItemSelection(
    val userProfile: UserProfile,
    var userEmojiSelected: Int? = null
)
