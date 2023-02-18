package com.samirmaciel.queridometroapp.view.register.viewModel

import android.net.Uri
import androidx.core.net.toUri
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

    var inputUserNameStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    var inputEmailStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    var inputPasswordStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    var inputRepeatedPasswordStatus: MutableLiveData<InputStatus> = MutableLiveData(InputStatus.INPUT_NOT_FILL)
    var imageCaptured: MutableLiveData<Uri?> = MutableLiveData()
    var imageCapturedListener: MutableLiveData<Boolean> = MutableLiveData(true)

    val allInputStatus: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(inputUserNameStatus){ _ -> this.value = validAllInputs() }
        addSource(inputEmailStatus){ _ -> this.value = validAllInputs() }
        addSource(inputPasswordStatus){ _ -> this.value = validAllInputs() }
        addSource(inputRepeatedPasswordStatus){ _ -> this.value = validAllInputs() }
        addSource(imageCaptured){ _ -> this.value = validAllInputs() }
    }

    private fun validAllInputs() : Boolean {
        var inputIsValid = false

        inputIsValid = inputUserNameStatus.value == InputStatus.INPUT_FILL &&
                inputEmailStatus.value == InputStatus.INPUT_FILL &&
                inputPasswordStatus.value == InputStatus.INPUT_FILL &&
                inputRepeatedPasswordStatus.value == InputStatus.INPUT_FILL

        if(inputIsValid && imageCaptured.value == null){
            inputIsValid = false
            imageCapturedListener?.value = false
        }else{
            inputIsValid = true
            imageCapturedListener?.value = true
        }

        return inputIsValid

    }

    fun validateInputUserName(userName: String){
        var value: InputStatus? = null

        if(userName.length >= 5){
            value = InputStatus.INPUT_FILL
        }else{
            value = InputStatus.INPUT_MINIMUM_LENTH
        }

        if(userName.contains(" ")){
            value = InputStatus.INPUT_HAS_SPACE
        }
        inputUserNameStatus.value = value
    }

    fun validateInputEmail(email: String){
        var isValid = false
        var value = InputStatus.INPUT_INVALID_EMAIL

        val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        isValid = emailRegex.matches(email)

        if(isValid){
            value = InputStatus.INPUT_FILL
        }

        inputEmailStatus.value = value
    }

    fun validateInputPassword(password: String){
        var value = InputStatus.INPUT_MINIMUM_LENTH
        if(password.trim().length >= 8){
            value = InputStatus.INPUT_FILL
        }

        inputPasswordStatus.value = value
    }

    fun validateInputRepeatedPassword(repeatedPassword: String, password: String){
        var value = InputStatus.INPUT_PASSWORD_NOT_EQUAL
        if(repeatedPassword == password){
            value = InputStatus.INPUT_FILL
        }

        if(repeatedPassword.trim().length < 8){
            value = InputStatus.INPUT_MINIMUM_LENTH
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
                            val userProfile = UserProfile(userName, fireBaseUser.uid, null, null, it, mutableListOf())
                            registerUserProfile(userProfile){
                                if(it){
                                    mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener {authResult ->
                                        onFinish(Pair(it, "Sucesso ao registrar usuário!"))
                                    }
                                }

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

        if(userProfile.userID == null){
            onFinish(false)
            return
        }

        mFireStore.collection("users").document(userProfile.userID!!).set(userProfile).addOnCompleteListener {
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