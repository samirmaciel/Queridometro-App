package com.samirmaciel.queridometroapp.model.FireBaseModels

import com.samirmaciel.queridometroapp.model.Emoji
import java.util.*

data class UserProfile(
    var userName : String? = null,
    var userID: String? = null,
    var roomIDEntered: String? = null,
    var roomIDCreated: String? = null,
    var profileImage : String? = null,
    var statusEmojiList: MutableList<Emoji>? = null
)
