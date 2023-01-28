package com.samirmaciel.queridometroapp.view.home.adpter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.model.UserProfileItem
import de.hdodenhof.circleimageview.CircleImageView

class UserCarouselAdapter : RecyclerView.Adapter<UserCarouselAdapter.MyViewHolder>() {

    var userProfileItemList: List<UserProfileItem> = listOf()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val userImage = itemView.findViewById<CircleImageView>(R.id.ciCarouselUserImage)
        val userName = itemView.findViewById<TextView>(R.id.txtCarouselUserName)
        val rvEmoji = itemView.findViewById<RecyclerView>(R.id.rvCarouselEmoji)

        fun onBindItem(userProfileItem: UserProfileItem){
            Glide.with(itemView.context).load(userProfileItem.profileImage).into(userImage)
            userName.text = userProfileItem.userName

            val emojiAdapter = EmojiCarouselAdapter()

            rvEmoji.apply {
                adapter = emojiAdapter
                layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            }

            emojiAdapter.emojiCarouselItemList = userProfileItem.emojiList
            emojiAdapter.notifyDataSetChanged()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_carousel_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(userProfileItemList == null) 0 else Int.MAX_VALUE
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBindItem(userProfileItemList[position % userProfileItemList.size])
    }
}