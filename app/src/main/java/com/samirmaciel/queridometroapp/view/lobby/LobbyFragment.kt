package com.samirmaciel.queridometro.view.lobby

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.samirmaciel.queridometro.R
import com.samirmaciel.queridometro.databinding.FragmentLobbyBinding


class LobbyFragment : Fragment(R.layout.fragment_lobby) {

    private var mBinding : FragmentLobbyBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBinding(view)
    }

    private fun setupBinding(view: View) {
        mBinding = FragmentLobbyBinding.bind(view)
    }


    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }
}