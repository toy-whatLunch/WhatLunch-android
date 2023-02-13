package com.sungbin.whatlunch_android.network.api

import com.sungbin.whatlunch_android.network.data.BasicData
import com.sungbin.whatlunch_android.network.data.LoginData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkService {
    @POST("/socialLogin")
    suspend fun requestToken(@Body data: LoginData): Response<BasicData>
}