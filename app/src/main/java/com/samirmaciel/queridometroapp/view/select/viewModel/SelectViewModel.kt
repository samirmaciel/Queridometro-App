package com.samirmaciel.queridometroapp.view.select.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.queridometroapp.model.Emoji
import com.samirmaciel.queridometroapp.model.FireBaseModels.Room
import com.samirmaciel.queridometroapp.model.FireBaseModels.RoomMember
import com.samirmaciel.queridometroapp.model.FireBaseModels.UserProfile
import com.samirmaciel.queridometroapp.model.roomMemberEmojiSelection
import java.util.*

class SelectViewModel : ViewModel() {

    val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var mRoomUserListForSelectAdapter: MutableLiveData<MutableList<RoomMember>>? = MutableLiveData()
    var mCurrentUserProfile: MutableLiveData<UserProfile>? = MutableLiveData()
    var mRoomEntered: MutableLiveData<Room>? = MutableLiveData()
    var mListenerNewUsers: MutableLiveData<String?> = MutableLiveData()
    var mRoomMemberList: MutableLiveData<MutableList<RoomMember>>? = MutableLiveData()

    fun initializeViewModel(roomMemberList: MutableLiveData<MutableList<RoomMember>>?, roomEntered: MutableLiveData<Room>?, currentUserProfile: MutableLiveData<UserProfile>?){
        mRoomEntered = roomEntered
        mCurrentUserProfile = currentUserProfile
        mRoomMemberList = roomMemberList
        mRoomMemberList?.value = roomEntered?.value?.roomMembersList
        listenerNewMembers()
    }

    fun validateSelectionUsersEmoji(list: List<roomMemberEmojiSelection>): Boolean{
        var result = true
        list.forEach {
            if(it.userEmojiSelected == null) result = false
        }

        return result
    }
    fun validateWhoCurrentUserNotSelectedEmojiToday(oldRoomMemberList: MutableList<RoomMember>?){
        var newRoomMemberList: MutableList<RoomMember> = mutableListOf()

        if(oldRoomMemberList.isNullOrEmpty()){
            mRoomUserListForSelectAdapter?.value = mutableListOf()
        }

        if(!roomDateIsValid(mRoomEntered?.value)){
            oldRoomMemberList?.forEach {roomMember ->
                roomMember.usersWhoSelectedEmoji = mutableListOf()
            }


            val newRoom = cleanRoomMembersEmojis(mRoomEntered?.value)
            newRoom?.roomDate = Calendar.getInstance().time
            newRoom?.roomMembersList = oldRoomMemberList

            updateRoom(newRoom)
        }

        for(roomMember in oldRoomMemberList ?: mutableListOf()){

            if(roomMember.usersWhoSelectedEmoji.isNullOrEmpty()){
                newRoomMemberList.add(roomMember)
            }else{
                var userHasSelectedEmojiToday = false
                roomMember.usersWhoSelectedEmoji?.forEach { userID ->
                    if(userID.equals(mCurrentUserProfile?.value?.userID)){
                        userHasSelectedEmojiToday = true
                    }
                }

                if(!userHasSelectedEmojiToday){
                    newRoomMemberList.add(roomMember)
                }
            }
        }

        val result = newRoomMemberList.filter { !it.userID.equals(mCurrentUserProfile?.value?.userID) }.toMutableList()

        mRoomUserListForSelectAdapter?.value = result

    }

    private fun cleanRoomMembersEmojis(room: Room?): Room?{
        room?.roomMembersList?.forEach { roomMember ->
            roomMember.selectedEmojiesList = mutableListOf()
        }

        return room
    }

    private fun roomDateIsValid(room: Room?): Boolean{
        if(room?.roomDate == null) return true
        val roomDate = room.roomDate

        val actualDay = Calendar.getInstance().time.day
        val actualMonth = Calendar.getInstance().time.month
        val actualYear = Calendar.getInstance().time.year

        val roomDateDay = roomDate?.day
        val roomDateMonth = roomDate?.month
        val roomDateYear = roomDate?.year

        if(actualDay != roomDateDay) {
            return false
        }

        if(actualMonth != roomDateMonth){
            return false
        }

        if(actualYear != roomDateYear){
            return false
        }

        return true

    }

    fun updateRoomMembers(roomMemberEmojiSelectionList: List<roomMemberEmojiSelection>){
        val roomMembersList = mRoomEntered?.value?.roomMembersList ?: mutableListOf()

        roomMembersList.forEach {roomMember ->
            roomMemberEmojiSelectionList.forEach { userProfileItemSelection ->

                if(roomMember.userID.equals(userProfileItemSelection.member.userID)){
                    val emojiList = roomMember.selectedEmojiesList ?: mutableListOf()
                    var hasEmoji = false
                    emojiList.forEach {
                        if(it.emojiID?.equals(userProfileItemSelection.userEmojiSelected) == true){
                            hasEmoji = true
                        }
                    }

                    if(hasEmoji){
                        emojiList.forEach { emoji ->
                            if(userProfileItemSelection.userEmojiSelected?.equals(emoji.emojiID) == true){
                                emoji.count = emoji.count?.plus(1)
                            }
                        }
                    }else{
                        emojiList.add(Emoji(userProfileItemSelection.userEmojiSelected, 1))
                    }

                    roomMember.selectedEmojiesList = emojiList
                    roomMember.usersWhoSelectedEmoji?.add(mCurrentUserProfile?.value?.userID!!)
                }
            }
        }

        val newRoom = mRoomEntered?.value
        newRoom?.roomMembersList = roomMembersList
        mRoomEntered?.value = newRoom
        updateRoom(newRoom)
    }

    private fun updateRoom(room: Room?){
        if(room?.roomID == null) return
        mRoomEntered?.value = room
        mFireStore.collection("rooms").document(room.roomID!!).set(room).addOnSuccessListener {

        }
    }

    fun roomMembersIsEmpty(roomMemberMutableList: MutableList<RoomMember>?): Boolean {
        var isEmpty = true

        roomMemberMutableList?.forEach {
            if(!it.userID.equals(mCurrentUserProfile?.value?.userID)){
                isEmpty = false
            }
        }

        return isEmpty
    }

    private fun listenerNewMembers(){
        val query = mFireStore.collection(mRoomEntered?.value?.roomID!!)
        query.addSnapshotListener{snapshots, e ->
            if(snapshots != null && !snapshots.documents.isNullOrEmpty()){
                val room = snapshots.documents.first()?.toObject(Room::class.java)
                mListenerNewUsers.value = room?.usersIDsParticipants?.size.toString()
            }
        }
    }

    fun clearViewModel(){
        mRoomMemberList?.value = null
    }
}