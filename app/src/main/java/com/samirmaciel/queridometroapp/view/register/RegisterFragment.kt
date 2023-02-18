package com.samirmaciel.queridometro.view.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentRegisterBinding
import com.samirmaciel.queridometroapp.util.InputStatus
import com.samirmaciel.queridometroapp.view.register.viewModel.RegisterViewModel


class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var mBinding: FragmentRegisterBinding? = null
    private var mViewModel: RegisterViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBinding(view)
        setupViewModels()
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {
        mViewModel?.allInputStatus?.observe(viewLifecycleOwner){
            buttonConfirmVisibility(it)
        }

        mViewModel?.inputUserNameStatus?.observe(viewLifecycleOwner){
            when(it){
                InputStatus.INPUT_FILL -> {
                    mBinding?.edtRegisterName?.setTextColor(resources.getColor(R.color.green))
                    mBinding?.txtRegisterNotificationUserName?.visibility = View.INVISIBLE
                }

                InputStatus.INPUT_MINIMUM_LENTH -> {
                    mBinding?.edtRegisterName?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.txtRegisterNotificationUserName?.text = resources.getString(R.string.title_username_minimum_lenth)
                    mBinding?.txtRegisterNotificationUserName?.visibility = View.VISIBLE
                }

                InputStatus.INPUT_HAS_SPACE -> {
                    mBinding?.edtRegisterName?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.txtRegisterNotificationUserName?.text = resources.getString(R.string.title_name_not_can_has_space)
                    mBinding?.txtRegisterNotificationUserName?.visibility = View.VISIBLE
                }

                else ->{
                    mBinding?.edtRegisterName?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.txtRegisterNotificationUserName?.visibility = View.INVISIBLE
                }
            }
        }

        mViewModel?.inputEmailStatus?.observe(viewLifecycleOwner){
            when(it){
                InputStatus.INPUT_FILL -> {
                    mBinding?.edtRegisterEmail?.setTextColor(resources.getColor(R.color.green))
                    mBinding?.txtRegisterNotificationEmail?.visibility = View.INVISIBLE
                }

                InputStatus.INPUT_INVALID_EMAIL -> {
                    mBinding?.edtRegisterEmail?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.txtRegisterNotificationEmail?.visibility = View.VISIBLE
                }

                else ->{
                    mBinding?.edtRegisterEmail?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.txtRegisterNotificationEmail?.visibility = View.INVISIBLE
                }
            }
        }

        mViewModel?.inputPasswordStatus?.observe(viewLifecycleOwner){
            when(it){
                InputStatus.INPUT_FILL -> {
                    mBinding?.txtRegisterNotificationPassword?.visibility = View.INVISIBLE
                    if(mBinding?.edtRegisterRepeatPassword?.text.toString().length == mBinding?.edtRegisterPassword?.text.toString().length){
                        mBinding?.edtRegisterPassword?.setTextColor(resources.getColor(R.color.green))
                        mBinding?.edtRegisterRepeatPassword?.setTextColor(resources.getColor(R.color.green))
                        mBinding?.txtRegisterNotificationRepeatedPassword?.visibility = View.INVISIBLE
                    }

                    if(mBinding?.edtRegisterRepeatPassword?.text.toString().isNotEmpty() && !mBinding?.edtRegisterRepeatPassword?.text.toString().equals(mBinding?.edtRegisterPassword?.text.toString())){
                        mViewModel?.inputRepeatedPasswordStatus?.value = InputStatus.INPUT_PASSWORD_NOT_EQUAL
                    }

                }

                InputStatus.INPUT_MINIMUM_LENTH -> {
                    mBinding?.edtRegisterPassword?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.txtRegisterNotificationPassword?.visibility = View.VISIBLE
                    mBinding?.edtRegisterRepeatPassword?.setTextColor(resources.getColor(R.color.white))
                }

                else -> {
                    mBinding?.edtRegisterPassword?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.txtRegisterNotificationPassword?.visibility = View.INVISIBLE
                }
            }
        }

        mViewModel?.inputRepeatedPasswordStatus?.observe(viewLifecycleOwner){
            when(it){
                InputStatus.INPUT_FILL -> {
                    mBinding?.edtRegisterPassword?.setTextColor(resources.getColor(R.color.green))
                    mBinding?.edtRegisterRepeatPassword?.setTextColor(resources.getColor(R.color.green))
                    mBinding?.txtRegisterNotificationRepeatedPassword?.visibility = View.INVISIBLE
                }

                InputStatus.INPUT_PASSWORD_NOT_EQUAL -> {
                    mBinding?.edtRegisterPassword?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.edtRegisterRepeatPassword?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.txtRegisterNotificationRepeatedPassword?.apply {
                        visibility = View.VISIBLE
                        text = resources.getString(R.string.title_password_not_equal)
                    }
                }

                InputStatus.INPUT_MINIMUM_LENTH -> {
                    mBinding?.edtRegisterRepeatPassword?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.edtRegisterPassword?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.txtRegisterNotificationRepeatedPassword?.apply {
                        visibility = View.VISIBLE
                        text = resources.getString(R.string.title_password_minimum_lenth)
                    }
                }

                else -> {
                    mBinding?.edtRegisterRepeatPassword?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.edtRegisterPassword?.setTextColor(resources.getColor(R.color.white))
                    mBinding?.txtRegisterNotificationRepeatedPassword?.visibility = View.INVISIBLE
                }
            }
        }

        mViewModel?.imageCapturedListener?.observe(viewLifecycleOwner){
            if(it == true){
                mBinding?.txtRegisterNotificationImage?.visibility = View.INVISIBLE
            }else{
                mBinding?.txtRegisterNotificationImage?.visibility = View.VISIBLE
            }
        }

    }

    private fun buttonConfirmVisibility(state: Boolean){

        if(state){
            mBinding?.btnRegisterConfirm?.apply{
                setBackgroundColor(resources.getColor(R.color.primary))
                setTextColor(resources.getColor(R.color.white))
                isEnabled = true
            }
        }else{
            mBinding?.btnRegisterConfirm?.isEnabled = false
            mBinding?.btnRegisterConfirm?.apply{
                setBackgroundColor(resources.getColor(R.color.hint))
                setTextColor(resources.getColor(R.color.black))
            }
        }
    }

    private fun setupListeners() {
        mBinding?.edtRegisterName?.doOnTextChanged { name, _, _, _ -> mViewModel?.validateInputUserName(name.toString()) }
        mBinding?.edtRegisterEmail?.doOnTextChanged { email, _, _, _ -> mViewModel?.validateInputEmail(email.toString())  }
        mBinding?.edtRegisterPassword?.doOnTextChanged { password, _, _, _ -> mViewModel?.validateInputPassword(password.toString())  }
        mBinding?.edtRegisterRepeatPassword?.doOnTextChanged { repeatedPassword, _, _, _ -> mViewModel?.validateInputRepeatedPassword(repeatedPassword.toString(), mBinding?.edtRegisterPassword?.text.toString())  }

        mBinding?.cvRegisterImage?.setOnClickListener{
            captureImage()
        }

        mBinding?.btnRegisterConfirm?.setOnClickListener {
            isLoadingCreatingUser(true)
            mViewModel?.registerUser(mBinding?.edtRegisterName?.text.toString(), mBinding?.edtRegisterEmail?.text.toString(), mBinding?.edtRegisterPassword?.text.toString()){
                isLoadingCreatingUser(false)
                findNavController().navigate(R.id.action_registerFragment_to_lobbyFragment)
                Toast.makeText(requireContext(), it.second, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun captureImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*");
        startActivityForResult(intent, 1)
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode === 1) {
            try {
                val capturedImage = data?.data
                //capturedImage.compress(Bitmap.CompressFormat.PNG, 100, streamImagem)
                mBinding?.cvRegisterImage?.setImageURI(capturedImage)
                mViewModel?.imageCaptured?.value = capturedImage
            } catch (e: Exception) {
                Log.d("TESTEIMGE", "onActivityResult: " + e.message)
            }
        }
    }

    private fun isLoadingCreatingUser(value: Boolean){
        mBinding?.btnRegisterConfirm?.isEnabled = !value
        mBinding?.edtRegisterName?.isEnabled = !value
        mBinding?.edtRegisterEmail?.isEnabled = !value
        mBinding?.edtRegisterPassword?.isEnabled = !value
        mBinding?.edtRegisterRepeatPassword?.isEnabled = !value
        mBinding?.cvRegisterImage?.isEnabled = !value

        mBinding?.progressLoadCreatingUser?.visibility = if(value) View.VISIBLE else View.GONE
        mBinding?.txtCreatingUser?.visibility = if(value) View.VISIBLE else View.GONE
    }

    private fun setupViewModels() {
        mViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    private fun setupBinding(view: View) {
        mBinding = FragmentRegisterBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

}