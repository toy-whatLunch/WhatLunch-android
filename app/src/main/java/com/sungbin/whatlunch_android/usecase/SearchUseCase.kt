package com.sungbin.whatlunch_android.usecase

import com.sungbin.whatlunch_android.repository.SearchRepository

class GetSearchCategoryUseCase(private val repo: SearchRepository) {
    suspend operator fun invoke(lon: String, lat: String, page: Int? =1) = repo.getSearchCategory(lon, lat, page)
}