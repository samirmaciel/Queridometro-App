package com.samirmaciel.queridometro.view.lobby

import android.app.AlertDialog
import android.os.Bundle
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

        setupBinding(view)
        setupViewModels()
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        mViewModel?.mCurrentUserProfile?.observe(viewLifecycleOwner){
            mViewModel?.loadRoom(it.roomIDEntered)
            setupUserProfile(it)
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
                Toast.makeText(requireContext(), "A sala que você estava ${roomIDEntered} foi excluida", Toast.LENGTH_LONG).show()
            }
        }else if(roomIDCreated != null){
            mBinding?.btnRoomEntered?.text = "Você está na sala ${roomIDEntered}"
            mBinding?.edtLobbyRoomID?.visibility = View.GONE
            mBinding?.btnRoomEntered?.visibility = View.VISIBLE
            mBinding?.btnLobbyCreateNewRoom?.text = resources.getText(R.string.title_delete_my_room)
        }else{
            mBinding?.btnRoomEntered?.text = "Você está na sala ${roomIDEntered}"
            mBinding?.edtLobbyRoomID?.visibility = View.GONE
            mBinding?.btnRoomEntered?.visibility = View.VISIBLE
            mBinding?.btnLobbyCreateNewRoom?.text = resources.getText(R.string.title_get_out_room)
        }
    }

    private fun setupUserProfile(userProfile: UserProfile?) {
        Glide.with(requireContext()).load(userProfile?.profileImage).into(mBinding?.ivLobbyUserImage!!)
        mBinding?.txtLobbyUserName?.text = userProfile?.userName
    }

    private fun setupListeners() {

        mBinding?.btnLobbyMyStatus?.setOnClickListener {
            Toast.makeText(requireContext(), "Calma calma internauta, em breve você poderá ver seu status!", Toast.LENGTH_LONG).show()
        }
        mBinding?.btnLobbyLogout?.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext()).apply {
                setTitle("Deseja realmente sair?")
                setPositiveButton("Sim"){_, _ ->
                    if(mViewModel?.logout() == true) findNavController().navigate(R.id.action_lobbyFragment_to_loginFragment)
                }
                setNegativeButton("Não", null)
            }.show()
        }

        mBinding?.btnLobbyEnterRoom?.setOnClickListener {
            var roomID: String = ""
            val roomIDEntered = mViewModel?.mCurrentUserProfile?.value?.roomIDEntered
            val roomIDCreated = mViewModel?.mCurrentUserProfile?.value?.roomIDCreated

            if(roomIDCreated != null){
                roomID = roomIDCreated
            }else if(roomIDEntered != null){
                roomID = roomIDEntered
            }else{
                roomID = mBinding?.edtLobbyRoomID?.text.toString()
            }

            mViewModel?.enterRoom(roomID){
                if(it.first){
                    findNavController().navigate(R.id.action_lobbyFragment_to_selectFragment)
                }else{
                    Toast.makeText(requireContext(), it.second, Toast.LENGTH_LONG).show()
                }
            }

        }

        mBinding?.btnLobbyCreateNewRoom?.setOnClickListener {
            val roomIDEntered = mViewModel?.mCurrentUserProfile?.value?.roomIDEntered
            val roomIDCreated = mViewModel?.mCurrentUserProfile?.value?.roomIDCreated

            if(roomIDCreated == null && roomIDEntered == null) {
                isLoadingCreatingRoom(true)
                mViewModel?.createRoom {
                    isLoadingCreatingRoom(false)
                    if (it.first) {
                        Toast.makeText(
                            requireContext(),
                            "Sua sala foi criada com sucesso!",
                            Toast.LENGTH_LONG
                        ).show()
                        findNavController().navigate(R.id.action_lobbyFragment_to_selectFragment)
                    } else {
                        Toast.makeText(requireContext(), it.second, Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                if(roomIDCreated != null){
                    val alertDialog = AlertDialog.Builder(requireContext()).apply {
                        setTitle("Deseja realmente apagar sua sala?")
                        setPositiveButton("Sim"){_,_ ->
                            mViewModel?.deleteRoom()
                        }
                        setNegativeButton("Não", null)
                    }.show()
                }else{
                    val alertDialog = AlertDialog.Builder(requireContext()).apply {
                        setTitle("Deseja realmente sair da sala?")
                        setPositiveButton("Sim"){_,_ ->
                            mViewModel?.exitRoom()
                        }
                        setNegativeButton("Não", null)
                    }.show()
                }
            }
        }
    }

    private fun validateRoomIDField(text: String): Boolean {
        return !text.isNullOrEmpty() && text.length > 8
    }

    private fun isLoadingCreatingRoom(value: Boolean){
        mBinding?.progressLoadCreatingRoom?.visibility = if(value) View.VISIBLE else View.GONE
        mBinding?.txtCreatingYourRoom?.visibility = if(value) View.VISIBLE else View.GONE

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

    private fun setupBinding(view: View) {
        mBinding = FragmentLobbyBinding.bind(view)
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}