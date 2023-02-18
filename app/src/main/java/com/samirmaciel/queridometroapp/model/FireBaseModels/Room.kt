package com.samirmaciel.queridometroapp.model.FireBaseModels

import java.util.*

data class Room(
    var roomID: String? = null,
    var userCreatorID: String? = null,
    var roomDate: Date? = null,
    var usersIDsParticipants: MutableList<String?>? = null,
    var roomMembersList: MutableList<RoomMember>? = null
)
