package com.samirmaciel.queridometroapp.view.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentLoginBinding
import com.samirmaciel.queridometroapp.view.login.viewModel.LoginViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var mBinding : FragmentLoginBinding? = null
    private var mViewModel : LoginViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBinding(view)
        setupViewModels()
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        mViewModel?.allInputStatus?.observe(viewLifecycleOwner){ allInputStatus ->
            buttonEnterVisibility(allInputStatus)
        }
    }

    private fun setupViewModels() {
        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    private fun setupListeners() {

        buttonEnterVisibility(false)

        mBinding?.edtLoginEmail?.doOnTextChanged { email, _, _, _-> mViewModel?.setInputEmailStatus(mViewModel?.validateInputEmail(email.toString())) }
        mBinding?.edtLoginPassword?.doOnTextChanged{password, _,_,_ -> mViewModel?.setInputPasswordStatus(mViewModel?.validateInputPassword(password.toString()))}

        mBinding?.btnLoginEnter?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_selectFragment)
        }

        mBinding?.txtLoginNewRegister?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setupBinding(view: View) {
        mBinding = FragmentLoginBinding.bind(view)
    }

    private fun buttonEnterVisibility(state: Boolean){

        if(state){
            mBinding?.btnLoginEnter?.apply{
                setBackgroundColor(resources.getColor(R.color.primary))
                setTextColor(resources.getColor(R.color.white))
                isEnabled = true
            }
        }else{
            mBinding?.btnLoginEnter?.isEnabled = false
            mBinding?.btnLoginEnter?.apply{
                setBackgroundColor(resources.getColor(R.color.hint))
                setTextColor(resources.getColor(R.color.black))
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}