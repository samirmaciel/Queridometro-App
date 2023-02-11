package com.samirmaciel.queridometro.view.lobby

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentLobbyBinding
import com.samirmaciel.queridometroapp.view.lobby.viewModel.LobbyViewModel
import com.samirmaciel.queridometroapp.view.viewModel.SharedViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.Executor


class LobbyFragment : Fragment(R.layout.fragment_lobby) {

    private var mBinding : FragmentLobbyBinding? = null
    private var mViewModel: LobbyViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBinding(view)
        setupViewModels()
        setupListeners()
    }

    private fun setupListeners() {
        mBinding?.btnLobbyEnterRoom?.setOnClickListener {
            findNavController().navigate(R.id.action_lobbyFragment_to_selectFragment)
        }

        mBinding?.btnLobbyCreateNewRoom?.setOnClickListener {
            createNewRoom()
        }
    }

    private fun createNewRoom(){
        isLoadingCreatingRoom(true)
        val handle = Handler().postDelayed(Runnable {
            isLoadingCreatingRoom(false)
        }, 2000)


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
        val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        mViewModel = ViewModelProvider(this).get(LobbyViewModel::class.java)
        mViewModel?.initializeViewModel(sharedViewModel)
    }

    private fun setupBinding(view: View) {
        mBinding = FragmentLobbyBinding.bind(view)
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}