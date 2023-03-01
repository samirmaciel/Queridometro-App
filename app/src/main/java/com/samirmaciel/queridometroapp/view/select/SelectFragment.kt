package com.samirmaciel.queridometro.view.select

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentSelectBinding
import com.samirmaciel.queridometroapp.model.FireBaseModels.RoomMember
import com.samirmaciel.queridometroapp.model.roomMemberEmojiSelection
import com.samirmaciel.queridometroapp.util.CustomSnackBar
import com.samirmaciel.queridometroapp.view.select.adapter.UserSelectAdapter
import com.samirmaciel.queridometroapp.view.select.viewModel.SelectViewModel
import com.samirmaciel.queridometroapp.view.viewModel.SharedViewModel


class SelectFragment : Fragment(R.layout.fragment_select) {

    private var mBinding: FragmentSelectBinding? = null
    private var sharedViewModel: SharedViewModel? = null
    private var mViewModel: SelectViewModel? = null
    private var selectEmojiAdapter: UserSelectAdapter? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackButton(view)
        setupBinding(view)
        setupViewModel()
        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        buttonContinueVisibility(false)

        mBinding?.btnSelectExitRoom?.setOnClickListener {
//            mViewModel?.clearViewModel()
//            findNavController().navigate(R.id.action_selectFragment_to_lobbyFragment)
            shareRoomID(mViewModel?.mRoomEntered?.value?.roomID)
        }

        mBinding?.btnSelectContinue?.setOnClickListener {
            mViewModel?.updateRoomMembers(selectEmojiAdapter?.roomMemberEmojiSelectionItemList)
            findNavController().navigate(R.id.action_selectFragment_to_homeFragment)
        }
    }

    private fun setupObservers() {
        mViewModel?.mRoomUserListForSelectAdapter?.observe(viewLifecycleOwner){
            Log.d("TESTESELECT", "setupObservers: " + it.size)
            if(it.isNullOrEmpty()){
                if(mViewModel?.roomMembersIsEmpty(mViewModel?.mRoomEntered?.value?.roomMembersList) == true){
                    mBinding?.txtSelectFriend?.text = "Sua sala não possui membros, convide seus amigos! O ID da sua sala é ${mViewModel?.mRoomEntered?.value?.roomID}"
                    setupAdapter(it)
                }else{
                    buttonContinueVisibility(true)
                    mBinding?.txtSelectFriend?.text = "Você já selecionou emojis para todos hoje!"
                }
            }else{
                mBinding?.txtSelectFriend?.text = resources.getString(R.string.title_select_friend)
                setupAdapter(it)
            }
        }

        mViewModel?.mRoomEntered?.observe(viewLifecycleOwner){

            val result = sharedViewModel?.getNewMembersOrMembersWhoLeft(mViewModel?.mOldRoomEntered, it)

            if(result !=  null){
                if(result?.first.isNullOrEmpty() == false || !result?.second.isNullOrEmpty()){


                    result?.first?.forEach {
                        CustomSnackBar.getNotification(requireView(), requireContext(), "${it.userName} acabou de entrar!", false)
                    }

                    result?.second?.forEach {
                        CustomSnackBar.getNotification(requireView(), requireContext(), "${it.userName} acabou de sair!", true)
                    }

                    mViewModel?.validateWhoCurrentUserNotSelectedEmojiToday(it.roomMembersList)
                }
            }else{
                mViewModel?.validateWhoCurrentUserNotSelectedEmojiToday(it.roomMembersList)
            }
        }

        mViewModel?.mListenerNewUsers?.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAdapter(roomMemberList: List<RoomMember>){
        selectEmojiAdapter = UserSelectAdapter(childFragmentManager){
            if(mViewModel?.validateSelectionUsersEmoji(it) == true){
                buttonContinueVisibility(true)
            }
        }

        mBinding?.rvSelectFriend?.adapter = selectEmojiAdapter
        mBinding?.rvSelectFriend?.layoutManager = GridLayoutManager(requireContext(), 3)

        selectEmojiAdapter?.roomMemberEmojiSelectionItemList = roomMemberList.map { roomMemberEmojiSelection(it) }
        selectEmojiAdapter?.notifyDataSetChanged()
    }

    private fun buttonContinueVisibility(state: Boolean){
        if(state){
            mBinding?.btnSelectContinue?.apply{
                setBackgroundColor(resources.getColor(R.color.primary))
                setTextColor(resources.getColor(R.color.white))
                isEnabled = true
            }
        }else{
            mBinding?.btnSelectContinue?.isEnabled = false
            mBinding?.btnSelectContinue?.apply{
                setBackgroundColor(resources.getColor(R.color.hint))
                setTextColor(resources.getColor(R.color.black))
            }
        }
    }

    private fun setupViewModel() {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        mViewModel = ViewModelProvider(this).get(SelectViewModel::class.java)
        mViewModel?.initializeViewModel(sharedViewModel?.roomMemberList, sharedViewModel?.roomEntered, sharedViewModel?.currentUserProfile)
    }

    private fun setupBinding(view: View) {
        mBinding = FragmentSelectBinding.bind(view)
    }

    private fun shareRoomID(roomID: String?){
        if(roomID == null) return
        val message = "Venha para minha sala no Queridometro, o ID é $roomID"
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, message)
        sendIntent.type = "text/plain"
        sendIntent.setPackage("com.whatsapp")

        try{
            startActivity(sendIntent)
        }catch (ex: ActivityNotFoundException){
            CustomSnackBar.getNotification(requireView(), requireContext(), "WhatsApp não foi inicializado em seu dispositivo!", false)
        }
    }

    private fun setupBackButton(view: View){
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener{ v, keyCode, event ->
            if(event.action === KeyEvent.ACTION_DOWN){
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    mViewModel?.clearViewModel()
                    findNavController().navigate(R.id.action_selectFragment_to_lobbyFragment)
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