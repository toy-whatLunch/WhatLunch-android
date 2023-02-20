package com.sungbin.whatlunch_android.network.api

import com.sungbin.whatlunch_android.BuildConfig
import com.sungbin.whatlunch_android.network.data.KakaoData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoSearchService {
    @GET("v2/local/search/keyword.json")
    suspend fun getSearchKeyword(
        @Query("query") query: String,
        @Query("x") lon: String,
        @Query("y") lat: String,
        @Query("page") page: Int? = 1,
        @Query("radius") radius: Int? = 20000,
        @Query("size") size: Int? = 15)
    : Response<KakaoData>

    @GET("v2/local/search/category.json")
    suspend fun getSearchCategory(
        @Header("Authorization") key: String?= BuildConfig.kakao_api_key,
        @Query("category_group_code") category: String?= "FD6",
        @Query("x") lon: String,
        @Query("y") lat: String,
        @Query("page") page: Int? = 1,
        @Query("radius") radius: Int? = 1000,
        @Query("size") size: Int? = 15)
            : Response<KakaoData>
}