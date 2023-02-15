package com.sungbin.whatlunch_android.network.api

import com.sungbin.whatlunch_android.network.data.BasicData
import com.sungbin.whatlunch_android.network.data.LoginData
import retrofit2.Response
import retrofit2.http.*

interface NetworkService {
    @POST("socialLogin")
    suspend fun requestToken(@Body data: LoginData): Response<BasicData>

    @FormUrlEncoded
    @PUT("device")
    suspend fun updateFcmToken(@Field("fcmToken") fcmToken: String): Response<BasicData>

    @GET("member/info")
    suspend fun getUser(): Response<BasicData>
}