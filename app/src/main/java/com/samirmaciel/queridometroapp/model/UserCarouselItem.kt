package com.samirmaciel.queridometroapp.model

data class UserCarouselItem(
    val userName : String,
    val profileImage : String,
    val emojiList : List<EmojiCarouselItem>
)
