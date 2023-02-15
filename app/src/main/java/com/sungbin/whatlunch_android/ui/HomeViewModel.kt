package com.sungbin.whatlunch_android.ui

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.sungbin.whatlunch_android.base.HiltBaseViewModel
import com.sungbin.whatlunch_android.network.data.MyUser
import com.sungbin.whatlunch_android.usecase.GetUserUseCase
import com.sungbin.whatlunch_android.usecase.PutFcmTokenUseCase
import com.sungbin.whatlunch_android.util.LOG_TAG
import com.sungbin.whatlunch_android.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val putFcmTokenUseCase: PutFcmTokenUseCase
): HiltBaseViewModel() {

    private val _user = MutableStateFlow<UiState<MyUser>>(UiState.Empty)
    val user = _user.asStateFlow()

    fun getUser() = viewModelScope.launch {
        val result = getUserUseCase.invoke()

        _user.value = UiState.Loading
        result?.let {
            if(it.success){
                val myUser = convertData<MyUser>(it.data?: "")
                _user.value = UiState.Success(myUser)
            }else{
                _user.value = UiState.Error(it.msg)
            }
        }
        _user.value = UiState.Empty
    }

    fun registerFcmToken(existedToken: String?)= viewModelScope.launch {
        val currentToken = FirebaseMessaging.getInstance().token.await()

        if (currentToken.isNullOrEmpty() || currentToken.isBlank()) return@launch

        Log.d(LOG_TAG, "currentToken = $currentToken")

        if (existedToken != currentToken) {
            putFcmTokenUseCase.invoke(fcmToken = currentToken)
        }
    }
}