package com.samirmaciel.queridometro.view.lobby

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentLobbyBinding


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