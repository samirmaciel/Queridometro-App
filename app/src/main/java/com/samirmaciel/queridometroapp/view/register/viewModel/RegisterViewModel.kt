package com.samirmaciel.queridometroapp.view.register.viewModel

import android.provider.MediaStore.Audio.Media
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.samirmaciel.queridometroapp.util.InputStatus

class RegisterViewModel : ViewModel() {

    private var inputNameStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    private var inputEmailStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    private var inputPasswordStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    private var inputRepeatedPasswordStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    val allInputStatus: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(inputNameStatus){ _ -> this.value = validAllInputs() }
        addSource(inputEmailStatus){ _ -> this.value = validAllInputs() }
        addSource(inputPasswordStatus){ _ -> this.value = validAllInputs() }
        addSource(inputRepeatedPasswordStatus){ _ -> this.value = validAllInputs() }
    }

    private fun validAllInputs() : Boolean {
        return inputNameStatus.value == InputStatus.INPUT_FILL &&
                inputEmailStatus.value == InputStatus.INPUT_FILL &&
                inputPasswordStatus.value == InputStatus.INPUT_FILL &&
                inputRepeatedPasswordStatus.value == InputStatus.INPUT_FILL
    }

    fun validateInputName(name: String){
        var value = InputStatus.INPUT_NOT_FILL
        if(name.trim().length >= 10){
            value = InputStatus.INPUT_FILL
        }
        inputNameStatus.value = value
    }

    fun validateInputEmail(email: String){
        var value = InputStatus.INPUT_NOT_FILL
        if(email.trim().length >= 10){
            value = InputStatus.INPUT_FILL
        }
        inputEmailStatus.value = value
    }

    fun validateInputPassword(password: String){
        var value = InputStatus.INPUT_NOT_FILL
        if(password.trim().length >= 8){
            value = InputStatus.INPUT_FILL
        }
        inputPasswordStatus.value = value
    }

    fun validateInputRepeatedPassword(repeatedPassword: String, password: String){
        var value = InputStatus.INPUT_NOT_FILL
        if(repeatedPassword.trim().length >= 8 && repeatedPassword == password){
            value = InputStatus.INPUT_FILL
        }
        inputRepeatedPasswordStatus.value = value
    }
}