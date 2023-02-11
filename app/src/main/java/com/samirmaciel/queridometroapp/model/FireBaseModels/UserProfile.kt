package com.samirmaciel.queridometroapp.model.FireBaseModels

import com.samirmaciel.queridometroapp.model.Emoji
import java.util.*

data class UserProfile(
    val userName : String,
    val userID: String,
    var lastDateSelected: Date? = null,
    var roomID: String? = null,
    val profileImage : String,
    var currentEmojiList : MutableList<Emoji>,
    var usersSelectedEmojiList: MutableList<UserEmojiSelection>? = null,
    var statusEmojiList: MutableList<Emoji>? = null
)
