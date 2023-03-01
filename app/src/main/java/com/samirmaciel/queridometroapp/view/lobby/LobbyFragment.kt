package com.samirmaciel.queridometro.view.lobby

import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentLobbyBinding
import com.samirmaciel.queridometroapp.model.FireBaseModels.Room
import com.samirmaciel.queridometroapp.model.FireBaseModels.UserProfile
import com.samirmaciel.queridometroapp.view.lobby.viewModel.LobbyViewModel
import com.samirmaciel.queridometroapp.view.viewModel.SharedViewModel


class LobbyFragment : Fragment(R.layout.fragment_lobby) {

    private var mBinding : FragmentLobbyBinding? = null
    private var mSharedViewModel: SharedViewModel? = null
    private var mViewModel: LobbyViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton(view)
        setupBinding(view)
        setupViewModels()
        setupListeners()
        setupObservers()
        isLoadingLobby(true)
    }

    private fun setupObservers() {
        mViewModel?.mCurrentUserProfile?.observe(viewLifecycleOwner){
            if(it.userID != null){
                mViewModel?.loadRoom(it.roomIDEntered)
                setupUserProfile(it)
                isLoadingCreatingRoom(false)
            }else{
                getToastMessage("Error ao carregar usuário, tente novamente!")
                findNavController().navigate(R.id.action_lobbyFragment_to_loginFragment)
            }

        }

        mViewModel?.mRoomEntered?.observe(viewLifecycleOwner){
            updateUI(it)
        }
    }

    private fun updateUI(room: Room?){
        val roomIDEntered = mViewModel?.mCurrentUserProfile?.value?.roomIDEntered
        val roomIDCreated = mViewModel?.mCurrentUserProfile?.value?.roomIDCreated
        if(room == null){
            mBinding?.edtLobbyRoomID?.visibility = View.VISIBLE
            mBinding?.btnRoomEntered?.visibility = View.GONE
            mBinding?.btnLobbyCreateNewRoom?.text = resources.getText(R.string.title_create_new_room)
            if(roomIDEntered != null) {
                mViewModel?.cleanUserProfileRoom()
                getToastMessage(resources.getString(R.string.title_your_room_was_deleted, roomIDEntered))
            }
        }else if(roomIDCreated != null){
            mBinding?.btnRoomEntered?.text = resources.getString(R.string.title_your_room_is, roomIDEntered)
            mBinding?.edtLobbyRoomID?.visibility = View.GONE
            mBinding?.btnRoomEntered?.visibility = View.VISIBLE
            mBinding?.btnLobbyCreateNewRoom?.text = resources.getText(R.string.title_delete_my_room)
        }else if(roomIDEntered != null){
            mBinding?.btnRoomEntered?.text = resources.getString(R.string.title_your_room_is, roomIDEntered)
            mBinding?.edtLobbyRoomID?.visibility = View.GONE
            mBinding?.btnRoomEntered?.visibility = View.VISIBLE
            mBinding?.btnLobbyCreateNewRoom?.text = resources.getText(R.string.title_get_out_room)
        }
    }

    private fun setupListeners() {

        mBinding?.btnLobbyMyStatus?.setOnClickListener {
            getToastMessage(resources.getString(R.string.message_my_status_comming_son))
        }
        mBinding?.btnLobbyLogout?.setOnClickListener {
            getAlertLogout()
        }

        mBinding?.btnLobbyEnterRoom?.setOnClickListener {
            enterRoom()
        }

        mBinding?.btnLobbyCreateNewRoom?.setOnClickListener {
            createOrDeleteRoom()
        }
    }

    private fun setupUserProfile(userProfile: UserProfile?) {
        Glide.with(requireContext()).load(userProfile?.profileImage).into(mBinding?.ivLobbyUserImage!!)
        mBinding?.txtLobbyUserName?.text = userProfile?.userName
    }

    private fun createOrDeleteRoom() {
        val roomIDEntered = mViewModel?.mCurrentUserProfile?.value?.roomIDEntered
        val roomIDCreated = mViewModel?.mCurrentUserProfile?.value?.roomIDCreated

        if(roomIDCreated == null && roomIDEntered == null) {
            isLoadingCreatingRoom(true)
            mViewModel?.createRoom {
                isLoadingCreatingRoom(false)
                if (it.first) {
                    getToastMessage(resources.getString(R.string.title_room_created_with_success))
                    findNavController().navigate(R.id.action_lobbyFragment_to_selectFragment)
                } else {
                    getToastMessage(it.second)
                }
            }
        }else{
            if(roomIDCreated != null){
                getAlertDeleteRoom()
            }else{
                getAlertExitRoom()
            }
        }
    }

    private fun enterRoom() {
        val roomIDEntered = mViewModel?.mCurrentUserProfile?.value?.roomIDEntered
        val roomIDCreated = mViewModel?.mCurrentUserProfile?.value?.roomIDCreated

        if(roomIDCreated != null){
            mViewModel?.enterRoom(roomIDCreated){
                if(it.first){
                    findNavController().navigate(R.id.action_lobbyFragment_to_selectFragment)
                }else{
                    getToastMessage(it.second)
                }
            }
        }else if(roomIDEntered != null){
            mViewModel?.enterRoom(roomIDEntered){
                if(it.first){
                    findNavController().navigate(R.id.action_lobbyFragment_to_selectFragment)
                }else{
                    getToastMessage(resources.getString(R.string.title_your_room_was_deleted, roomIDEntered))
                    updateUI(null)
                }
            }
        }else{
            val roomID = mBinding?.edtLobbyRoomID?.text.toString()
            if(roomID.length > 1){
                mViewModel?.enterRoom(roomID){
                    if(it.first){
                        findNavController().navigate(R.id.action_lobbyFragment_to_selectFragment)
                    }else{
                        getToastMessage(it.second)
                    }
                }
            }else{
                getToastMessage(resources.getString(R.string.title_enter_with_room_id) , false)
            }

        }
    }

    private fun getAlertLogout() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle(resources.getString(R.string.title_question_log_out))
            setPositiveButton(resources.getString(R.string.title_yes)){_, _ ->
                if(mViewModel?.logout() == true) findNavController().navigate(R.id.action_lobbyFragment_to_loginFragment)
            }
            setNegativeButton(resources.getString(R.string.title_no), null)
        }.show()
    }

    private fun getAlertDeleteRoom() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle(resources.getString(R.string.title_question_delete_room))
            setPositiveButton(resources.getString(R.string.title_yes)){_,_ ->
                mViewModel?.deleteRoom()
            }
            setNegativeButton(resources.getString(R.string.title_no), null)
        }.show()
    }

    private fun isLoadingCreatingRoom(value: Boolean){
        mBinding?.progressLoadCreatingRoom?.visibility = if(value) View.VISIBLE else View.GONE
        mBinding?.txtCreatingYourRoom?.visibility = if(value) View.VISIBLE else View.GONE

        mBinding?.btnLobbyEnterRoom?.isEnabled = !value
        mBinding?.btnLobbyCreateNewRoom?.isEnabled = !value
        mBinding?.btnLobbyMyStatus?.isEnabled = !value
        mBinding?.edtLobbyRoomID?.isEnabled = !value
    }

    private fun isLoadingLobby(value: Boolean){
        mBinding?.progressLoadCreatingRoom?.visibility = if(value) View.VISIBLE else View.GONE

        mBinding?.btnLobbyEnterRoom?.isEnabled = !value
        mBinding?.btnLobbyCreateNewRoom?.isEnabled = !value
        mBinding?.btnLobbyMyStatus?.isEnabled = !value
        mBinding?.edtLobbyRoomID?.isEnabled = !value
    }

    private fun setupViewModels() {
        mSharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        mViewModel = ViewModelProvider(this).get(LobbyViewModel::class.java)
        mViewModel?.initializeViewModel(mSharedViewModel?.currentUserProfile, mSharedViewModel?.roomEntered)

    }

    private fun getAlertExitRoom(){
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setTitle(resources.getString(R.string.title_question_get_out_room))
            setPositiveButton(resources.getString(R.string.title_yes)){_,_ ->
                mViewModel?.exitRoom()
            }
            setNegativeButton(resources.getString(R.string.title_no), null)
        }.show()
    }

    private fun getToastMessage(message: String, isLong: Boolean? = null) {
        Toast.makeText(
            requireContext(),
            message,
            if(isLong == true) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        ).show()
    }

    private fun setupBinding(view: View) {
        mBinding = FragmentLobbyBinding.bind(view)
    }

    private fun setupBackButton(view: View){
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener{ v, keyCode, event ->
            if(event.action === KeyEvent.ACTION_DOWN){
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    getAlertLogout()
                    return@OnKeyListener true
                }
            }

            false
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}