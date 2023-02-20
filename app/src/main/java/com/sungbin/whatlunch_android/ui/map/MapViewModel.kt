package com.sungbin.whatlunch_android.ui.map

import androidx.lifecycle.viewModelScope
import com.sungbin.whatlunch_android.base.HiltBaseViewModel
import com.sungbin.whatlunch_android.network.data.KakaoData
import com.sungbin.whatlunch_android.usecase.GetSearchCategoryUseCase
import com.sungbin.whatlunch_android.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getSearchCategoryUseCase: GetSearchCategoryUseCase
): HiltBaseViewModel(){

    private val _kakaoData = MutableStateFlow<UiState<KakaoData>>(UiState.Empty)
    val kakaoData = _kakaoData.asStateFlow()

    fun getSearchCategory(lon: Double, lat: Double, page: Int?= 1) = viewModelScope.launch {
        val result = getSearchCategoryUseCase.invoke(lon.toString(), lat.toString(), page)

        _kakaoData.value = UiState.Loading

        if(result != null){
            val data = convertData<KakaoData>(result)
           _kakaoData.value = UiState.Success(data)
        }else{
            _kakaoData.value = UiState.Error("kakao error")
        }

        _kakaoData.value = UiState.Empty
    }
}