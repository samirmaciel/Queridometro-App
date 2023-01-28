package com.samirmaciel.queridometro.view.select

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentSelectBinding
import com.samirmaciel.queridometroapp.model.UserProfileItem
import com.samirmaciel.queridometroapp.view.select.adapter.UserSelectAdapter
import com.samirmaciel.queridometroapp.view.viewModel.SharedViewModel


class SelectFragment : Fragment(R.layout.fragment_select) {

    private var mBinding: FragmentSelectBinding? = null
    private var sharedViewModel: SharedViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBinding(view)
        setupViewModel()
        setupObservers()
    }

    private fun setupObservers() {
        sharedViewModel?.userProfileItemList?.observe(viewLifecycleOwner){
            setupAdapter(it)
        }
    }

    private fun setupAdapter(userProfileItemList: List<UserProfileItem>){
        var adapter = UserSelectAdapter()
        mBinding?.rvSelectFriend?.adapter = adapter
        mBinding?.rvSelectFriend?.layoutManager = GridLayoutManager(requireContext(), 3)

        adapter.userProfileItemList = userProfileItemList
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
    }

    private fun setupBinding(view: View) {
        mBinding = FragmentSelectBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}