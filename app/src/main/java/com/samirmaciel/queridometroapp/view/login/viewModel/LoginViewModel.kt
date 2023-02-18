package com.samirmaciel.queridometroapp.view.login.viewModel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.samirmaciel.queridometroapp.util.InputStatus

class LoginViewModel: ViewModel() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var inputEmailStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    private var inputPasswordStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)

    var allInputStatus: MediatorLiveData<Boolean> = MediatorLiveData()

    init {
        allInputStatus.addSource(inputPasswordStatus){
            allInputStatus.value = it == InputStatus.INPUT_FILL && inputEmailStatus.value == InputStatus.INPUT_FILL
        }
        allInputStatus.addSource(inputEmailStatus){
            allInputStatus.value = it == InputStatus.INPUT_FILL && inputPasswordStatus.value == InputStatus.INPUT_FILL
        }
    }

    fun getAllInputStatus(): InputStatus {
        if(inputEmailStatus.value == InputStatus.INPUT_FILL && inputPasswordStatus.value == InputStatus.INPUT_FILL) return InputStatus.INPUT_FILL

        return InputStatus.INPUT_NOT_FILL
    }

    fun validateInputEmail(email: String) : InputStatus {
        if(email.trim().length >= 10) return InputStatus.INPUT_FILL
        return InputStatus.INPUT_NOT_FILL
    }

    fun validateInputPassword(password: String) : InputStatus {
        if(password.trim().length >= 8) return InputStatus.INPUT_FILL
        return InputStatus.INPUT_NOT_FILL
    }

    fun checkCurrentUser(): Boolean {
        val fireBaseUser = mAuth.currentUser

        if(fireBaseUser != null) return true

        return false
    }
    fun setInputEmailStatus(inputStatus: InputStatus?){
        inputEmailStatus.value = inputStatus ?: InputStatus.INPUT_NOT_FILL
    }

    fun setInputPasswordStatus(inputStatus: InputStatus?){
        inputPasswordStatus.value = inputStatus ?: InputStatus.INPUT_NOT_FILL
    }

    fun makeLogin(userEmail: String, userPassword: String, onFinish: (Pair<Boolean, String>) -> Unit){
        mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener {

            it.addOnSuccessListener {
                onFinish(Pair(true, "Login efetuado com sucesso!"))
            }

            it.addOnFailureListener {
                onFinish(Pair(false, it.message.toString()))
            }
        }

    }














}