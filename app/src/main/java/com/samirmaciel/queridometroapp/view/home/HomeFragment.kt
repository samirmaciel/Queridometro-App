package com.samirmaciel.queridometro.view.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentHomeBinding
import com.samirmaciel.queridometroapp.model.FireBaseModels.UserProfile
import com.samirmaciel.queridometroapp.view.home.adpter.UserCarouselAdapter
import com.samirmaciel.queridometroapp.view.viewModel.SharedViewModel
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home) {

    private var mBinding: FragmentHomeBinding? = null
    private var mSharedViewModel: SharedViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBinding(view)
        setupViewModel()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()

        Timer().scheduleAtFixedRate(ScrollTimer(), 10, 10)

        mBinding?.btnHomeLogout?.setOnClickListener {
            mBinding?.rvHomeUsers?.scrollBy(0, 1)
        }
    }

    private fun setupObservers() {
        mSharedViewModel?.userProfileList?.observe(viewLifecycleOwner){
            setupAdapter(it)
        }

    }

    private fun setupViewModel() {
        mSharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    private fun setupAdapter(userProfileList: List<UserProfile>) {
        val usersAdapter = UserCarouselAdapter()

        mBinding?.rvHomeUsers?.apply {
            adapter = usersAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        usersAdapter.userProfileList = userProfileList
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

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}