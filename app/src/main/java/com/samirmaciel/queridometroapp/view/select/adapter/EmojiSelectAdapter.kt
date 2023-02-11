package com.samirmaciel.queridometroapp.view.select.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.model.EmojiSelectionItem

class EmojiSelectAdapter(val onSelectedEmoji: (EmojiSelectionItem) -> Unit) : RecyclerView.Adapter<EmojiSelectAdapter.MyViewHolder>() {

    var emojiSelectionItemList: MutableList<EmojiSelectionItem> = mutableListOf()

    inner class MyViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivSelectionEmoji = itemView.findViewById<ImageView>(R.id.ivSelectionEmoji)

        fun onBindItem(position: Int, onSelectedEmoji: (EmojiSelectionItem) -> Unit){
            ivSelectionEmoji.setImageResource(emojiSelectionItemList[position].emojiID)

            if(emojiSelectionItemList[position].selected){
                ivSelectionEmoji.layoutParams.width = 200
                ivSelectionEmoji.layoutParams.height = 200
            }else{
                ivSelectionEmoji.layoutParams.width = 140
                ivSelectionEmoji.layoutParams.height = 140
            }

            ivSelectionEmoji.setOnClickListener {

                onSelectedEmoji(emojiSelectionItemList[position])
                emojiSelectionItemList[position].selected = true

                clearSelectionEmoji(emojiSelectionItemList[position])
                notifyDataSetChanged()
            }


        }

    }

    private fun clearSelectionEmoji(emojiSelectionItem: EmojiSelectionItem){
        emojiSelectionItemList.forEach {
            if (it.emojiID != emojiSelectionItem.emojiID){
                it.selected = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_emoji_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return emojiSelectionItemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBindItem(position, onSelectedEmoji)
    }
}