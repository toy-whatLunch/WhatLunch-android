package com.sungbin.whatlunch_android.repository

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import com.sungbin.whatlunch_android.network.api.NetworkService
import com.sungbin.whatlunch_android.network.data.BasicData
import com.sungbin.whatlunch_android.network.data.LoginData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val networkService: NetworkService) :
    BaseRepository() {

//    suspend fun requestFireBaseToken(loginData: LoginData): String? {
//        val result = call(
//            call = { networkService.requestToken(data = loginData) },
//            error = "http get user error"
//        ) ?: return null
//
//        if(result.data == null)
//            return null
//
//        return getRemoteData<String>(result.data)
//    }

    suspend fun requestFireBaseToken(loginData: LoginData): BasicData? {
        val result = call(
            call = { networkService.requestToken(data = loginData) },
            error = "http FireBaseToken error"
        )

        return result
    }

    suspend fun updateFcmToken(fcmToken: String): BasicData? {
        val result = call(
            call = { networkService.updateFcmToken(fcmToken) },
            error = "http updateFcmToken error"
        )

        return result
    }

    suspend fun getUser(): BasicData? {

        return call(
            call = { networkService.getUser() },
            error = "http get user error"
        )
    }

    suspend fun requestIsKakaoTalk(context: Context): Boolean =
        UserApiClient.instance.isKakaoTalkLoginAvailable(context)

    suspend fun requestKakaoTalkLogin(
        context: Context,
        callback: (token: OAuthToken?, error: Throwable?) -> Unit
    ) = UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)

    suspend fun requestKakaoWebLogin(
        context: Context,
        callback: (token: OAuthToken?, error: Throwable?) -> Unit
    ) = UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)

    suspend fun getKakaoProfile(callback: (user: User?, error: Throwable?) -> Unit) =
        UserApiClient.instance.me(callback = callback)
}