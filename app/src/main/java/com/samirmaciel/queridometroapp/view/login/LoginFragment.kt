package com.samirmaciel.queridometroapp.view.login

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
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
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        if(mViewModel?.checkCurrentUser() == true){
            findNavController().navigate(R.id.action_loginFragment_to_lobbyFragment)
        }
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
            isLoadingLogin(true)
            mViewModel?.makeLogin(mBinding?.edtLoginEmail?.text.toString(), mBinding?.edtLoginPassword?.text.toString()){

                if(it.first){
                    isLoadingLogin(false)
                    findNavController().navigate(R.id.action_loginFragment_to_lobbyFragment)
                }else{
                    isLoadingLogin(false)
                    Toast.makeText(requireContext(), it.second, Toast.LENGTH_LONG).show()
                }
            }
        }

        mBinding?.txtLoginNewRegister?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        mBinding?.btnLoginHelp?.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Queridometro 1.2v")
                setMessage(resources.getString(R.string.message_about))
                setPositiveButton("Ok", null)
            }.show()
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

    private fun isLoadingLogin(isLoading: Boolean){
        mBinding?.progressLoadLogin?.visibility = if(isLoading) View.VISIBLE else View.GONE
        mBinding?.txtMakeLogin?.visibility = if(isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}