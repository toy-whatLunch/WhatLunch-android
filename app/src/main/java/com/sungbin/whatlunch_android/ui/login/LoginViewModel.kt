package com.sungbin.whatlunch_android.ui.login

import androidx.lifecycle.viewModelScope
import com.sungbin.whatlunch_android.base.HiltBaseViewModel
import com.sungbin.whatlunch_android.network.data.LoginData
import com.sungbin.whatlunch_android.usecase.PostSocialLoginUseCase
import com.sungbin.whatlunch_android.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postSocialLoginUseCase: PostSocialLoginUseCase
): HiltBaseViewModel(){

    private val _token = MutableStateFlow<UiState<String>>(UiState.Loading)
    val token = _token.asStateFlow()

    fun socialLogin(loginData: LoginData) = viewModelScope.launch {
        val result = postSocialLoginUseCase.invoke(loginData)

        _token.value = UiState.Loading
        result?.let {
           if(it.success){
               _token.value = UiState.Success(it.data as String)
           }else{
               _token.value = UiState.Error(it.msg)
           }
        }
        _token.value = UiState.Empty
    }
}