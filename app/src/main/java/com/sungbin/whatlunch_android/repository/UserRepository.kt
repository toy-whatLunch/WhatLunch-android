package com.sungbin.whatlunch_android.repository

import com.sungbin.whatlunch_android.network.api.NetworkService
import com.sungbin.whatlunch_android.network.data.LoginData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val networkService: NetworkService) :
    BaseRepository() {

    suspend fun requestFireBaseToken(loginData: LoginData): String? {
        val result = call(
            call = { networkService.requestToken(data = loginData) },
            error = "http get user error"
        ) ?: return null

        if(result.data == null)
            return null

        return getRemoteData<String>(result.data)
    }
}