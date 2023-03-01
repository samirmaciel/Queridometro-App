package com.samirmaciel.queridometroapp.view.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samirmaciel.queridometroapp.model.FireBaseModels.Room
import com.samirmaciel.queridometroapp.model.FireBaseModels.RoomMember
import com.samirmaciel.queridometroapp.model.FireBaseModels.UserProfile

open class SharedViewModel : ViewModel() {

    var roomMemberList: MutableLiveData<MutableList<RoomMember>> = MutableLiveData()
    var roomEntered: MutableLiveData<Room> = MutableLiveData()
    val currentUserProfile: MutableLiveData<UserProfile> = MutableLiveData()

    fun getNewMembersOrMembersWhoLeft(oldRoom: Room?, newRoom: Room): Pair<List<RoomMember>,List<RoomMember>>? {
        if(oldRoom == null) return null
        val oldRoomMemberList = oldRoom.roomMembersList

        var newMembersList: MutableList<RoomMember> = mutableListOf()
        var membersWhoLeftList: MutableList<RoomMember> = mutableListOf()

        if((oldRoomMemberList?.size ?: 0) < (newRoom.roomMembersList?.size ?: 0)){
            newRoom.roomMembersList?.forEach { roomMember ->
                val member = oldRoomMemberList?.filter { it.userID?.equals(roomMember.userID) == true }?.firstOrNull()
                if(member == null){
                    newMembersList.add(roomMember)
                }
            }
        }else if((oldRoomMemberList?.size ?: 0) > (newRoom.roomMembersList?.size ?: 0)){
            oldRoomMemberList?.forEach { roomMember ->
                val member = newRoom.roomMembersList?.filter { it.userID?.equals(roomMember.userID) == true }?.firstOrNull()
                if(member == null){
                    membersWhoLeftList.add(roomMember)
                }
            }
        }

        if(newMembersList.size == 0 && membersWhoLeftList.size == 0) return null

        return Pair(newMembersList.toList(), membersWhoLeftList.toList())

    }
}