package com.sungbin.whatlunch_android.base

import androidx.lifecycle.ViewModel
import com.google.gson.Gson

abstract class HiltBaseViewModel : ViewModel() {
    protected inline fun <reified T> convertData(data: Any): T {
        return Gson().fromJson(Gson().toJson(data), T::class.java)
    }
}