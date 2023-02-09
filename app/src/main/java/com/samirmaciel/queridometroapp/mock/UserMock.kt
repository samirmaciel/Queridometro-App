package com.samirmaciel.queridometroapp.mock

import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.model.EmojiCarouselItem
import com.samirmaciel.queridometroapp.model.UserProfileItem

object UserMock {

    var generalUserList: List<UserProfileItem> = getUserCarouselListItem()

    fun getUserCarouselListItem() : List<UserProfileItem>{
        return listOf<UserProfileItem>(
            UserProfileItem("Black", "https://s2.glbimg.com/HNtVguV5kfdFMyGdB-sAGcFDXa0=/i.s3.glbimg.com/v1/AUTH_e84042ef78cb4708aeebdf1c68c6cbd6/internal_photos/bs/2023/q/8/isGwrhTseVHYUkKFfvpA/cezar-foto-330-270-bbb23.png", getEmojiCarouselListItem()),
            UserProfileItem("Domitila", "https://s2.glbimg.com/bvcrAlxJWGrroFKlgd08dXTrMUU=/i.s3.glbimg.com/v1/AUTH_e84042ef78cb4708aeebdf1c68c6cbd6/internal_photos/bs/2023/D/B/1eIYhdRv2ymWBzulzKmw/domitila-barros-foto-330-270-bbb23.png", getEmojiCarouselListItem()),
            UserProfileItem("Fred", "https://s2.glbimg.com/eM0NqkWFwpdDzFw2rQuf0ScXP8M=/i.s3.glbimg.com/v1/AUTH_e84042ef78cb4708aeebdf1c68c6cbd6/internal_photos/bs/2023/f/J/bT3Vv0QZWAn5f5UepdqQ/fred-foto-330-270-bbb23.png", getEmojiCarouselListItem()),
            UserProfileItem("Nicácio", "https://s2.glbimg.com/Oq-kfI9lD9Qd1pn75esr4SlJdEw=/i.s3.glbimg.com/v1/AUTH_e84042ef78cb4708aeebdf1c68c6cbd6/internal_photos/bs/2023/s/9/Mdb40BTGAJsJ9fuoRvrg/nicacio-foto-330-270-bbb23.png", getEmojiCarouselListItem()),
            UserProfileItem("Larissa", "https://s2.glbimg.com/-caXoQsbWDAoKTooZMVTVncRGSs=/i.s3.glbimg.com/v1/AUTH_e84042ef78cb4708aeebdf1c68c6cbd6/internal_photos/bs/2023/O/A/MaQNodRIiGx8A8UmBdsQ/larissa-foto-330-270-bbb23.png", getEmojiCarouselListItem()),
            UserProfileItem("Gustavo", "https://s2.glbimg.com/hb1e_t0vJU09PCDH2u4_TuxapYY=/i.s3.glbimg.com/v1/AUTH_e84042ef78cb4708aeebdf1c68c6cbd6/internal_photos/bs/2023/p/M/FaP5NrR6it8n3nSQlD4Q/gustavo-foto-330-270-bbb23.png", getEmojiCarouselListItem()),
        )

    }

    fun getEmojiCarouselListItem(): MutableList<EmojiCarouselItem>{

       return mutableListOf(
//            EmojiCarouselItem(R.drawable.emojibanana, 2),
//            EmojiCarouselItem(R.drawable.emojiheart, 4),
//            EmojiCarouselItem(R.drawable.emojihappy, 8),
//            EmojiCarouselItem(R.drawable.emojiplant, 6),
        )
    }



}