package com.sungbin.whatlunch_android.repository

import com.sungbin.whatlunch_android.network.api.KakaoSearchService
import com.sungbin.whatlunch_android.network.data.KakaoData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(private val kakaoService: KakaoSearchService) :
    BaseRepository() {

    suspend fun getSearchKeyword() {

    }

    suspend fun getSearchCategory(lon: String, lat: String, page: Int?= 1): KakaoData? {
        val result = call(
            call = { kakaoService.getSearchCategory(lon = lon, lat = lat, page = page) },
            error = "http kakao category error"
        )

        return result
    }
}