package com.samirmaciel.queridometroapp.view.home.viewModel

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.queridometroapp.model.FireBaseModels.Room
import com.samirmaciel.queridometroapp.model.FireBaseModels.RoomMember
import com.samirmaciel.queridometroapp.model.FireBaseModels.UserProfile

class HomeViewModel : ViewModel() {

    private val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var mRoomEntered : MutableLiveData<Room>? = null
    var mListenerNewUsers: MutableLiveData<String?> = MutableLiveData()


    fun initializeViewModel(roomEntered: MutableLiveData<Room>?){
        mRoomEntered = roomEntered
        listenerNewMembers()
    }


    private fun listenerNewMembers(){
        val query = mFireStore.collection(mRoomEntered?.value?.roomID!!)
        query.addSnapshotListener{snapshots, e ->

            if(snapshots != null){
                var room: Room? = null
                for(doc in snapshots){
                    room = doc.toObject(Room::class.java)
                }

                if(room != null){
                    mListenerNewUsers.value = room?.usersIDsParticipants?.size.toString()
                }

            }

        }
    }
}