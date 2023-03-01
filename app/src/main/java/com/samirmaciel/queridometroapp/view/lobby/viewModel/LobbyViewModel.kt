package com.samirmaciel.queridometroapp.view.lobby.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.samirmaciel.queridometroapp.model.FireBaseModels.Room
import com.samirmaciel.queridometroapp.model.FireBaseModels.RoomMember
import com.samirmaciel.queridometroapp.model.FireBaseModels.RoomParameters
import com.samirmaciel.queridometroapp.model.FireBaseModels.UserProfile
import java.util.Calendar

class LobbyViewModel: ViewModel() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var mRoomEntered: MutableLiveData<Room>? = null
    var mCurrentUserProfile: MutableLiveData<UserProfile>? = null
    init {
        loadUserProfile()
    }
    fun initializeViewModel(currentUserProfile: MutableLiveData<UserProfile>?, roomEntered: MutableLiveData<Room>?){
        mRoomEntered = roomEntered
        mCurrentUserProfile = currentUserProfile
    }
    private fun loadUserProfile(){
        val fireBaseUser = mAuth.currentUser

        if(fireBaseUser != null){
            mFireStore.collection("users")
                .document(fireBaseUser.uid)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val userProfile = documentSnapshot.toObject(UserProfile::class.java)
                        mCurrentUserProfile?.value = userProfile
                    } else {

                    }
                }
                .addOnFailureListener { e ->
                    Log.d("TESTEGET", "loadUserProfile: " + e.message.toString())
                }
        }else{
            Log.d("TESTEGET", "Firebase User null")
        }
    }

    fun createRoom(onFinish: (Pair<Boolean, String>) -> Unit){

        loadAppParameters { roomParameters ->
            if(roomParameters != null){
                val roomID = "${mCurrentUserProfile?.value?.userName}${roomParameters.roomIDKeys}"
                val room = Room(roomID, mCurrentUserProfile?.value?.userID, Calendar.getInstance().time, mutableListOf())
                room.usersIDsParticipants?.add(mCurrentUserProfile?.value?.userID)
                saveRoom(room){
                    if(it.first){
                        roomParameters.roomIDKeys = roomParameters.roomIDKeys?.plus(1)
                        val userProfile = mCurrentUserProfile?.value
                        userProfile?.roomIDCreated = roomID
                        userProfile?.roomIDEntered = roomID
                        updateAppParameters(roomParameters)
                        updateUserProfile(userProfile)
                        addCurrentUserToRoomMembers(userProfile, room)
                        onFinish(it)
                    }else{
                        onFinish(it)
                    }
                }
            }else{
                onFinish(Pair(false, "Error RoomParameters"))
            }
        }
    }

    private fun addCurrentUserToRoomMembers(userProfile: UserProfile?, room: Room?) {
        val currentRoomMemberList = room?.roomMembersList ?: mutableListOf()
        var roomMemberExists = false

        currentRoomMemberList.forEach {
            if(it.userID.equals(userProfile?.userID)){
                roomMemberExists = true
            }
        }

        if(!roomMemberExists){
            currentRoomMemberList.add(RoomMember(userProfile?.userID, userProfile?.profileImage, userProfile?.userName, mutableListOf(), mutableListOf()))
        }

        room?.roomMembersList = currentRoomMemberList

        updateRoom(room)

    }

    private fun saveRoom(room: Room, onFinish: (Pair<Boolean, String>) -> Unit){
        mFireStore.collection("rooms").document(room.roomID!!).set(room).addOnSuccessListener {
            onFinish(Pair(true, "Sua sala foi registrada com sucesso!"))
            updateRoom(room)
        }.addOnFailureListener {
            onFinish(Pair(false, it.message.toString()))
        }
    }
    private fun updateAppParameters(roomParameters: RoomParameters){
        mFireStore.collection("appParameters").document("room").set(roomParameters).addOnSuccessListener {

        }
    }

    private fun updateUserProfile(userProfile: UserProfile?){
        if(userProfile == null) return
        mCurrentUserProfile?.value = userProfile
        mFireStore.collection("users").document(userProfile.userID!!).set(userProfile).addOnSuccessListener {
            mCurrentUserProfile?.value = userProfile
        }
    }

    private fun updateRoom(room: Room?){
        if(room == null) return
        mRoomEntered?.value = room
        mFireStore.collection("rooms").document(room.roomID!!).set(room).addOnSuccessListener {

        }.addOnFailureListener {

        }
    }

    private fun updateRoomToExit(room: Room?){
        if(room == null) return

        checkIfRoomExitis(room){
            if(it){
                mRoomEntered?.value = null
                mFireStore.collection("rooms").document(room.roomID!!).set(room).addOnSuccessListener {

                }.addOnFailureListener {

                }
            }
        }
    }

    private fun loadAppParameters(onFinish: (RoomParameters?) -> Unit){
        mFireStore.collection("appParameters").document("room").get().addOnSuccessListener {documentSnapshot ->
            if (documentSnapshot.exists()) {
                val roomParameters = documentSnapshot.toObject(RoomParameters::class.java)
                onFinish(roomParameters)
            } else {
                onFinish(null)
            }
        }
    }

    private fun checkIfRoomExitis(room: Room?, onResult: (Boolean) -> Unit){
        if(room?.roomID == null){
            onResult(false)
            return
        }

        mFireStore.collection("rooms").document(room.roomID!!).get().addOnSuccessListener {
            val room = it.toObject(Room::class.java)

            if(room != null){
                onResult(true)
            }else{
                onResult(false)
            }

        }.addOnFailureListener {
            onResult(false)
        }
    }

    fun loadRoom(roomID: String?){
        if(roomID == null){
            mRoomEntered?.value = null
            return
        }
        mFireStore.collection("rooms").document(roomID).get().addOnSuccessListener {
            val room = it.toObject(Room::class.java)

            mRoomEntered?.value = room

        }.addOnFailureListener {
            mRoomEntered?.value = null
        }
    }

    fun enterRoom(roomID: String?, onFinish: (Pair<Boolean, String>) -> Unit){

        if(roomID == null){
            onFinish(Pair(false, "Error ao entrar na sala"))
            return
        }

        mFireStore.collection("rooms").document(roomID).get().addOnSuccessListener {documentSnapshot ->

            if (documentSnapshot.exists()) {
                val room = documentSnapshot.toObject(Room::class.java)
                val userProfile = mCurrentUserProfile?.value
                userProfile?.roomIDEntered = room?.roomID

                val userIDsParticipants = room?.usersIDsParticipants?.filter { it.equals(userProfile?.userID) }

                if((userIDsParticipants?.size ?: 0) < 1){
                    room?.usersIDsParticipants?.add(userProfile?.userID)
                }

                updateUserProfile(userProfile)
                addCurrentUserToRoomMembers(userProfile, room)

                onFinish(Pair(true, "Você entrou na sala com sucesso!"))

            }else{
                onFinish(Pair(false, "Sala não encontrada!"))
            }

        }.addOnFailureListener {
            onFinish(Pair(false, it.message.toString()))
        }
    }
    fun deleteRoom(){
        val roomID = mCurrentUserProfile?.value?.roomIDCreated

        if(roomID != null){
            val docRef = mFireStore.collection("rooms").document(roomID)

            docRef.delete().addOnSuccessListener {
                val userProfile = mCurrentUserProfile?.value
                userProfile?.roomIDCreated = null
                userProfile?.roomIDEntered = null

                updateUserProfile(userProfile)
            }.addOnFailureListener {

            }
        }
    }

    fun logout(): Boolean{
        mAuth.signOut()
        return true
    }

    fun exitRoom(){
        val newUserProfile = mCurrentUserProfile?.value
        val newRoom = mRoomEntered?.value
        var roomMember: RoomMember? = null
        Log.d("DELETESAIR", "exitRoom: RoomID: ${newRoom?.roomID}" )
        newUserProfile?.roomIDEntered = null
        newUserProfile?.roomIDCreated = null
        Log.d("DELETESAIR", "exitRoom: RoomMemberListeSize: ${newRoom?.roomMembersList?.size}" )
        newRoom?.roomMembersList?.forEach {
            if(it.userID.equals(newUserProfile?.userID)){
                roomMember = it
            }

            it.usersWhoSelectedEmoji?.remove(newUserProfile?.userID)

        }

        newRoom?.roomMembersList?.remove(roomMember)
        newRoom?.usersIDsParticipants?.remove(newUserProfile?.userID)

        updateRoomToExit(newRoom)
        updateUserProfile(newUserProfile)

    }

    fun cleanUserProfileRoom() {
        val newUserProfile = mCurrentUserProfile?.value

        newUserProfile?.roomIDEntered = null
        newUserProfile?.roomIDCreated = null

        updateUserProfile(newUserProfile)
    }


}