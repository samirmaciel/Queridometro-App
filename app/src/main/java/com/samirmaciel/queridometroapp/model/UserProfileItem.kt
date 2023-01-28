package com.samirmaciel.queridometroapp.model

data class UserProfileItem(
    val userName : String,
    val profileImage : String,
    val emojiList : List<EmojiCarouselItem>
)
