package com.samirmaciel.queridometroapp.view.home.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.queridometroapp.model.FireBaseModels.Room
import com.samirmaciel.queridometroapp.model.FireBaseModels.RoomMember

class HomeViewModel : ViewModel() {

    private val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var mRoomEntered : MutableLiveData<Room>? = null
    var mOldRoomEntered: Room? = null
    var mListenerNewUsers: MutableLiveData<String?> = MutableLiveData()


    fun initializeViewModel(roomEntered: MutableLiveData<Room>?){
        mOldRoomEntered = roomEntered?.value
        mRoomEntered = roomEntered

        roomEntered?.value?.let {room ->
            listenerNewMembers(room.roomID)
        }
    }
    private fun listenerNewMembers(roomID: String?){
        if(roomID == null) return
        val docRef = mFireStore.collection("rooms").document(roomID)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d("TESTEFIREBASE", "error: ${e}")
            }

            if (snapshot != null && snapshot.exists()) {
                mOldRoomEntered = mRoomEntered?.value
                mRoomEntered?.value = snapshot.toObject(Room::class.java)
            }

        }
    }


}