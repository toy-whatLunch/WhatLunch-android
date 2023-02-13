package com.sungbin.whatlunch_android.usecase

import com.sungbin.whatlunch_android.network.data.LoginData
import com.sungbin.whatlunch_android.repository.UserRepository

class PostSocialLoginUseCase(private val repository: UserRepository){
    suspend operator fun invoke(loginData: LoginData) = repository.requestFireBaseToken(loginData)
}