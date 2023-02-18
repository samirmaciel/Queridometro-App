package com.samirmaciel.queridometroapp.model.FireBaseModels

import java.util.*

data class UserEmojiSelection(
    var roomID: String? = null,
    var userID: String? = null,
    var emojiID: Int? = null,
    var selectedDate: Date? = null
)
