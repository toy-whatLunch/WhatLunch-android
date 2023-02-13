package com.sungbin.whatlunch_android.ui.login

import androidx.lifecycle.viewModelScope
import com.sungbin.whatlunch_android.base.HiltBaseViewModel
import com.sungbin.whatlunch_android.network.data.LoginData
import com.sungbin.whatlunch_android.usecase.PostSocialLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.logging.Logger
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postSocialLoginUseCase: PostSocialLoginUseCase
): HiltBaseViewModel(){

    fun socialLogin(loginData: LoginData) = viewModelScope.launch {
        val result = postSocialLoginUseCase.invoke(loginData)

        result?.let {

        }
    }
}