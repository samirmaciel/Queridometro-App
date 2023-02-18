package com.samirmaciel.queridometro.view.select

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentSelectBinding
import com.samirmaciel.queridometroapp.model.FireBaseModels.RoomMember
import com.samirmaciel.queridometroapp.model.roomMemberEmojiSelection
import com.samirmaciel.queridometroapp.view.select.adapter.UserSelectAdapter
import com.samirmaciel.queridometroapp.view.select.viewModel.SelectViewModel
import com.samirmaciel.queridometroapp.view.viewModel.SharedViewModel


class SelectFragment : Fragment(R.layout.fragment_select) {

    private var mBinding: FragmentSelectBinding? = null
    private var sharedViewModel: SharedViewModel? = null
    private var mViewModel: SelectViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding(view)
        setupViewModel()
        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        buttonContinueVisibility(false)

        mBinding?.btnSelectExitRoom?.setOnClickListener {
//            val alertDialog = AlertDialog.Builder(requireContext()).apply {
//                setTitle("Deseja realmente sair da sala?")
//                setPositiveButton("Sim") { _, _ ->
//
//                }
//                setNegativeButton("Não", null)
//            }.show()
            mViewModel?.clearViewModel()
            findNavController().navigate(R.id.action_selectFragment_to_lobbyFragment)

        }

        mBinding?.btnSelectContinue?.setOnClickListener {
            findNavController().navigate(R.id.action_selectFragment_to_homeFragment)
        }
    }

    private fun setupObservers() {
        mViewModel?.mRoomUserListForSelectAdapter?.observe(viewLifecycleOwner){
            if(it.isNullOrEmpty()){
                if(mViewModel?.roomMembersIsEmpty(mViewModel?.mRoomEntered?.value?.roomMembersList) == true){
                    mBinding?.txtSelectFriend?.text = "Sua sala acabou de ser criada e não possui membros, convide seus amigos! O ID da sua sala é ${mViewModel?.mRoomEntered?.value?.roomID}"
                }else{
                    buttonContinueVisibility(true)
                    mBinding?.txtSelectFriend?.text = "Você já selecionou emojis para todos hoje!"
                }
            }else{
                setupAdapter(it)
            }

        }

        mViewModel?.mRoomMemberList?.observe(viewLifecycleOwner){
            mViewModel?.validateWhoCurrentUserNotSelectedEmojiToday(it)
        }

        mViewModel?.mListenerNewUsers?.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupAdapter(roomMemberList: List<RoomMember>){
        var adapter = UserSelectAdapter(childFragmentManager){

            if(mViewModel?.validateSelectionUsersEmoji(it) == true){
                buttonContinueVisibility(true)
                mViewModel?.updateRoomMembers(it)
            }
        }

        mBinding?.rvSelectFriend?.adapter = adapter
        mBinding?.rvSelectFriend?.layoutManager = GridLayoutManager(requireContext(), 3)

        adapter.roomMemberEmojiSelectionItemList = roomMemberList.map { roomMemberEmojiSelection(it) }
        adapter.notifyDataSetChanged()
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

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}