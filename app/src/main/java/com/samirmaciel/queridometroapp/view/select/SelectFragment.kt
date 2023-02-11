package com.samirmaciel.queridometro.view.select

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentSelectBinding
import com.samirmaciel.queridometroapp.mock.UserMock
import com.samirmaciel.queridometroapp.model.FireBaseModels.UserProfile
import com.samirmaciel.queridometroapp.model.UserProfileItemSelection
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

        mBinding?.btnSelectContinue?.setOnClickListener {
            findNavController().navigate(R.id.action_selectFragment_to_homeFragment)
        }
    }

    private fun setupObservers() {
        setupAdapter(UserMock.generalUserListProfile)
    }

    private fun setupAdapter(userProfileList: List<UserProfile>){
        var adapter = UserSelectAdapter(childFragmentManager){

            if(mViewModel?.validateSelectionUsersEmoji(it) == true){
                buttonContinueVisibility(true)
                mViewModel?.generatedUserProfileItemList(it)
            }

        }

        mBinding?.rvSelectFriend?.adapter = adapter
        mBinding?.rvSelectFriend?.layoutManager = GridLayoutManager(requireContext(), 3)

        adapter.userProfileItemList = userProfileList.map { UserProfileItemSelection(it) }
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
        mViewModel?.initializeViewModel(sharedViewModel?.userProfileList)
    }

    private fun setupBinding(view: View) {
        mBinding = FragmentSelectBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}