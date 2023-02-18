package com.samirmaciel.queridometroapp.model.FireBaseModels

import com.samirmaciel.queridometroapp.model.Emoji

data class RoomMember(
    var userID: String? = null,
    var userImage: String? = null,
    var userName: String? = null,
    var usersWhoSelectedEmoji: MutableList<String>? = null,
    var selectedEmojiesList: MutableList<Emoji>? = null
)
