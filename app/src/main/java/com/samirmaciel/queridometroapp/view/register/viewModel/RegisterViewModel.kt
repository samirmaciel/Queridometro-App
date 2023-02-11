package com.samirmaciel.queridometroapp.view.register.viewModel

import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.samirmaciel.queridometroapp.model.FireBaseModels.UserProfile
import com.samirmaciel.queridometroapp.util.InputStatus

class RegisterViewModel : ViewModel() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val mFireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val mStore: FirebaseStorage = FirebaseStorage.getInstance()

    private var inputNameStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    private var inputEmailStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    private var inputPasswordStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    private var inputRepeatedPasswordStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    var imageCaptured: MutableLiveData<Uri?> = MutableLiveData()

    val allInputStatus: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(inputNameStatus){ _ -> this.value = validAllInputs() }
        addSource(inputEmailStatus){ _ -> this.value = validAllInputs() }
        addSource(inputPasswordStatus){ _ -> this.value = validAllInputs() }
        addSource(inputRepeatedPasswordStatus){ _ -> this.value = validAllInputs() }
        addSource(imageCaptured){ _ -> this.value = validAllInputs() }
    }

    private fun validAllInputs() : Boolean {
        return inputNameStatus.value == InputStatus.INPUT_FILL &&
                inputEmailStatus.value == InputStatus.INPUT_FILL &&
                inputPasswordStatus.value == InputStatus.INPUT_FILL &&
                inputRepeatedPasswordStatus.value == InputStatus.INPUT_FILL &&
                imageCaptured.value != null
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

    fun registerUser(userName: String, userEmail: String, userPassword: String, onFinish: (Pair<Boolean, String>) -> Unit){

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener {

            it.addOnSuccessListener {
                val fireBaseUser = it.user

                if(fireBaseUser != null){
                    uploadUserImage(imageCaptured.value, fireBaseUser.uid){
                        if(it != null){
                            val userProfile = UserProfile(userName, fireBaseUser.uid, null, null, it, mutableListOf(), mutableListOf(), mutableListOf())
                            registerUserProfile(userProfile){
                                onFinish(Pair(it, "Sucesso ao registrar usuário!"))
                            }
                        }else{
                            onFinish(Pair(false, "Error upload Image"))
                        }
                    }
                }
            }

            it.addOnFailureListener {
                onFinish(Pair(false, it.message.toString()))
            }
        }
    }

    private fun registerUserProfile(userProfile: UserProfile, onFinish: (Boolean) -> Unit){

        mFireStore.collection("users").add(userProfile).addOnCompleteListener {
            it.addOnSuccessListener {
                onFinish(true)
            }

            it.addOnFailureListener {
                onFinish(false)
            }
        }
    }

    private fun uploadUserImage(imageUri: Uri?, userID: String, onFinish: (String?) -> Unit){

        if(imageUri == null){
            onFinish(null)
            return
        }


        val storageRef = mStore.reference
        val imagesRef = storageRef.child("userImages")
        val fileRef = imagesRef.child(userID)

        val uploadTask = fileRef.putFile(imageUri)

        uploadTask.addOnFailureListener {
            onFinish(null)
        }.addOnSuccessListener {

        }

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                onFinish(null)
                task.exception?.let {
                    throw it
                }
            }
            fileRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result

                onFinish(downloadUri.toString())
            } else {
                // Handle failures
            }
        }
    }
}