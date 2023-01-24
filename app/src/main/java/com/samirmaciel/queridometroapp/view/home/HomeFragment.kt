package com.samirmaciel.queridometro.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.samirmaciel.queridometro.R
import com.samirmaciel.queridometro.databinding.FragmentHomeBinding


class HomeFragment : Fragment(R.layout.fragment_home) {

    private var mBinding: FragmentHomeBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBinding(view)
    }

    private fun setupBinding(view: View) {
        mBinding = FragmentHomeBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}