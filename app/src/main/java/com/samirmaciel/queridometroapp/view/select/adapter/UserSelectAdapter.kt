package com.samirmaciel.queridometroapp.view.select.adapter

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.model.GlideApp
import com.samirmaciel.queridometroapp.model.UserProfileItem
import de.hdodenhof.circleimageview.CircleImageView


class UserSelectAdapter : RecyclerView.Adapter<UserSelectAdapter.MyViewHolder>() {

    var userProfileItemList: List<UserProfileItem> = listOf()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val userImage = itemView.findViewById<CircleImageView>(R.id.ciSelectUserImage)
        val userName = itemView.findViewById<TextView>(R.id.txtSelectUserName)

        fun onBindItem(userProfileItem: UserProfileItem){
            Glide.with(itemView.context).load(userProfileItem.profileImage).into(userImage);

            userName.text = userProfileItem.userName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_user_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userProfileItemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBindItem(userProfileItemList[position])
    }
}