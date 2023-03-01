package com.samirmaciel.queridometro.view.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentHomeBinding
import com.samirmaciel.queridometroapp.model.FireBaseModels.RoomMember
import com.samirmaciel.queridometroapp.util.CustomSnackBar
import com.samirmaciel.queridometroapp.view.home.adpter.UserCarouselAdapter
import com.samirmaciel.queridometroapp.view.home.viewModel.HomeViewModel
import com.samirmaciel.queridometroapp.view.viewModel.SharedViewModel
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home) {

    private var mBinding: FragmentHomeBinding? = null
    private var mSharedViewModel: SharedViewModel? = null
    private var mViewModel: HomeViewModel? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackButton(view)
        setupBinding(view)
        setupViewModel()
        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        mBinding?.btnArrowBackToLobby?.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_lobbyFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        Timer().scheduleAtFixedRate(ScrollTimer(), 10, 10)
    }

    private fun setupObservers() {

        mViewModel?.mRoomEntered?.observe(viewLifecycleOwner){

            val pairWithNewAndWhoLeftMembersList = mSharedViewModel?.getNewMembersOrMembersWhoLeft(mViewModel?.mOldRoomEntered, it)

            if(pairWithNewAndWhoLeftMembersList != null){
                pairWithNewAndWhoLeftMembersList.first.forEach {
                    CustomSnackBar.getNotification(requireView(), requireContext(), "${it.userName} acabou de entrar!", true)
                }

                pairWithNewAndWhoLeftMembersList.second.forEach {
                    CustomSnackBar.getNotification(requireView(), requireContext(), "${it.userName} acabou de sair!", false)
                }

                if(pairWithNewAndWhoLeftMembersList.first.size > 0){
                    findNavController().navigate(R.id.action_homeFragment_to_selectFragment)
                }
            }


            setupAdapter(it.roomMembersList?.toList())
            mBinding?.txtHomeRoomIDTitle?.text = "${resources.getString(R.string.title_room_id)} ${it.roomID}"
        }

        mViewModel?.mListenerNewUsers?.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewModel() {
        mSharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        mViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        mViewModel?.initializeViewModel(mSharedViewModel?.roomEntered)
    }

    private fun setupAdapter(roomMemberList: List<RoomMember>?) {
        if(roomMemberList == null) return
        val usersAdapter = UserCarouselAdapter()

        mBinding?.rvHomeUsers?.apply {
            adapter = usersAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        usersAdapter.roomMemberItemList = roomMemberList
        usersAdapter.notifyDataSetChanged()
    }

    inner class ScrollTimer() : TimerTask() {
        override fun run() {

            val mHandler = Handler(Looper.getMainLooper());

            mHandler.post(Runnable {
                mBinding?.rvHomeUsers?.scrollBy(0, 1)
            } )
        }

    }

    private fun setupBinding(view: View) {
        mBinding = FragmentHomeBinding.bind(view)
    }

    private fun setupBackButton(view: View){
        view.isFocusableInTouchMode = true
        view.requestFocus()
        view.setOnKeyListener(View.OnKeyListener{ v, keyCode, event ->
            if(event.action === KeyEvent.ACTION_DOWN){
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    findNavController().navigate(R.id.action_homeFragment_to_selectFragment)
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