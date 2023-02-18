package com.samirmaciel.queridometroapp.view.home.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.model.Emoji

class EmojiCarouselAdapter : RecyclerView.Adapter<EmojiCarouselAdapter.MyViewHolder>() {

    var emojiList: MutableList<Emoji> = mutableListOf()

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val emojiImage = itemView.findViewById<ImageView>(R.id.ivEmojiCarousel)
        val txtCountEmoji = itemView.findViewById<TextView>(R.id.txtCountEmojiCarousel)

        fun onBindItem(emoji: Emoji){
            emojiImage.setImageResource(emoji.emojiID!!)
            txtCountEmoji.text = emoji.count.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.emoji_carousel_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return emojiList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBindItem(emojiList[position])
    }
}