package com.samirmaciel.queridometroapp.view.home.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.model.EmojiCarouselItem

class EmojiCarouselAdapter : RecyclerView.Adapter<EmojiCarouselAdapter.MyViewHolder>() {

    var emojiCarouselItemList: List<EmojiCarouselItem> = listOf()

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val emojiImage = itemView.findViewById<ImageView>(R.id.ivEmojiCarousel)
        val txtCountEmoji = itemView.findViewById<TextView>(R.id.txtCountEmojiCarousel)

        fun onBindItem(emojiCarouselItem: EmojiCarouselItem){
            emojiImage.setImageResource(emojiCarouselItem.emojiID)
            txtCountEmoji.text = emojiCarouselItem.count.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.emoji_carousel_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return emojiCarouselItemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBindItem(emojiCarouselItemList[position])
    }
}