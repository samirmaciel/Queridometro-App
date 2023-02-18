package com.samirmaciel.queridometroapp.view.select.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.model.roomMemberEmojiSelection
import com.samirmaciel.queridometroapp.view.select.selection.SelectionEmojiFragment
import de.hdodenhof.circleimageview.CircleImageView


class UserSelectAdapter(val childFragmentManager: FragmentManager, val onChangeList: (List<roomMemberEmojiSelection>) -> Unit) : RecyclerView.Adapter<UserSelectAdapter.MyViewHolder>() {

    var roomMemberEmojiSelectionItemList: List<roomMemberEmojiSelection> = listOf()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val userImage = itemView.findViewById<CircleImageView>(R.id.ciSelectUserImage)
        val emojiImage = itemView.findViewById<ImageView>(R.id.ciSelectedEmojiImage)
        val userName = itemView.findViewById<TextView>(R.id.txtSelectUserName)

        fun onBindItem(position: Int, onChangeList: (List<roomMemberEmojiSelection>) -> Unit){
            Glide.with(itemView.context).load(roomMemberEmojiSelectionItemList[position].member.userImage).into(userImage);

            userName.text = roomMemberEmojiSelectionItemList[position].member.userName

            if(roomMemberEmojiSelectionItemList[position].userEmojiSelected != null){
                emojiImage.setImageResource(roomMemberEmojiSelectionItemList[position].userEmojiSelected!!)
            }

            userImage.setOnClickListener {

                SelectionEmojiFragment {
                    roomMemberEmojiSelectionItemList[position].userEmojiSelected = it.emojiID
                    onChangeList(roomMemberEmojiSelectionItemList)
                    notifyItemChanged(position)
                }.show(childFragmentManager, "SelectionEmojiFragment")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_user_item, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return roomMemberEmojiSelectionItemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBindItem(position, onChangeList)
    }
}