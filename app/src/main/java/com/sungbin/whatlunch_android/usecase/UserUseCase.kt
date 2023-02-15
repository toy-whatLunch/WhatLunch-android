package com.sungbin.whatlunch_android.usecase

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.model.User
import com.sungbin.whatlunch_android.network.data.LoginData
import com.sungbin.whatlunch_android.repository.UserRepository

class PostSocialLoginUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(loginData: LoginData) = repo.requestFireBaseToken(loginData)
}

class PutFcmTokenUseCase(private val repo: UserRepository){
    suspend operator fun invoke(fcmToken: String) = repo.updateFcmToken(fcmToken)
}

class GetUserUseCase(private val repo: UserRepository){
    suspend operator fun invoke() = repo.getUser()
}

class IsKakaoTalkUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(context: Context): Boolean {
        return repo.requestIsKakaoTalk(context)
    }
}

class RunKakaoTalkLoginUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(context: Context, callback: (token: OAuthToken?, error: Throwable?) -> Unit) {
        repo.requestKakaoTalkLogin(context, callback)
    }
}

class RunKakaoWebLoginUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(context: Context, callback: (token: OAuthToken?, error: Throwable?) -> Unit) {
        repo.requestKakaoWebLogin(context, callback)
    }
}

class GetKakaoUserProfileUseCase(private val repo: UserRepository) {
    suspend operator fun invoke(callback: (user: User?, error: Throwable?) -> Unit) {
        repo.getKakaoProfile(callback)
    }
}
