package com.sungbin.whatlunch_android.ui.map

import androidx.lifecycle.viewModelScope
import com.sungbin.whatlunch_android.base.HiltBaseViewModel
import com.sungbin.whatlunch_android.usecase.GetSearchCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getSearchCategoryUseCase: GetSearchCategoryUseCase
): HiltBaseViewModel(){

    fun getSearchCategory(lon: Double, lat: Double, page: Int?= 1) = viewModelScope.launch {
        val result = getSearchCategoryUseCase.invoke(lon.toString(), lat.toString(), page)

        result?.let {

        }
    }
}