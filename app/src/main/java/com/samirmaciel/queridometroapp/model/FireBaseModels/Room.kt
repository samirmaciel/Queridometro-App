package com.samirmaciel.queridometroapp.model.FireBaseModels

data class Room(
    val roomID: String,
    val userCreatorID: String,
    val usersIDsParticipants: Array<String>
)
