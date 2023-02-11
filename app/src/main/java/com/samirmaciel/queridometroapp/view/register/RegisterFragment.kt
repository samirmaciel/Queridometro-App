package com.samirmaciel.queridometro.view.register

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.samirmaciel.queridometroapp.R
import com.samirmaciel.queridometroapp.databinding.FragmentRegisterBinding
import com.samirmaciel.queridometroapp.view.register.viewModel.RegisterViewModel
import java.io.ByteArrayOutputStream


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
        mBinding?.edtRegisterName?.doOnTextChanged { name, _, _, _ -> mViewModel?.validateInputName(name.toString())  }
        mBinding?.edtRegisterEmail?.doOnTextChanged { email, _, _, _ -> mViewModel?.validateInputEmail(email.toString())  }
        mBinding?.edtRegisterPassword?.doOnTextChanged { password, _, _, _ -> mViewModel?.validateInputPassword(password.toString())  }
        mBinding?.edtRegisterRepeatPassword?.doOnTextChanged { repeatedPassword, _, _, _ -> mViewModel?.validateInputRepeatedPassword(repeatedPassword.toString(), mBinding?.edtRegisterPassword?.text.toString())  }

        mBinding?.cvRegisterImage?.setOnClickListener{
            captureImage()
        }

        mBinding?.btnRegisterConfirm?.setOnClickListener {
            mViewModel?.registerUser(mBinding?.edtRegisterName?.text.toString(), mBinding?.edtRegisterEmail?.text.toString(), mBinding?.edtRegisterPassword?.text.toString()){
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