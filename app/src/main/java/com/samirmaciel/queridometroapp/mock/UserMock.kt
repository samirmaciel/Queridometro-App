package com.samirmaciel.queridometroapp.mock

import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.model.EmojiCarouselItem
import com.samirmaciel.queridometroapp.model.UserCarouselItem

object UserMock {

    fun getUserCarouselListItem() : List<UserCarouselItem>{
        return listOf<UserCarouselItem>(
            UserCarouselItem("Black", "link", getEmojiCarouselListItem()),
            UserCarouselItem("Domitila", "link", getEmojiCarouselListItem()),
            UserCarouselItem("Fred", "link", getEmojiCarouselListItem()),
            UserCarouselItem("Nicácio", "link", getEmojiCarouselListItem()),
            UserCarouselItem("Marilia", "link", getEmojiCarouselListItem()),
            UserCarouselItem("Gustavo", "link", getEmojiCarouselListItem()),
        )

    }

    fun getEmojiCarouselListItem(): List<EmojiCarouselItem>{

       return listOf<EmojiCarouselItem>(
            EmojiCarouselItem(R.drawable.emojibanana, 2),
            EmojiCarouselItem(R.drawable.emojiheart, 4),
            EmojiCarouselItem(R.drawable.emojihappy, 8),
            EmojiCarouselItem(R.drawable.emojiplant, 6),
        )
    }



}