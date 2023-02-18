package com.samirmaciel.queridometroapp.model

import com.samirmaciel.queridometroapp.model.FireBaseModels.RoomMember

data class roomMemberEmojiSelection(
    val member: RoomMember,
    var userEmojiSelected: Int? = null
)
