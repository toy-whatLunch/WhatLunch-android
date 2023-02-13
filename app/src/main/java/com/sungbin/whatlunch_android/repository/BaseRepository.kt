package com.sungbin.whatlunch_android.repository

import com.google.gson.Gson
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

open class BaseRepository @Inject constructor() {
    protected suspend fun <T : Any> call(call: suspend () -> Response<T>, error: String): T? {

        val response = call.invoke()

        val result = if (response.isSuccessful) {
            RemoteData.Success(response.body()!!)
        } else {
            RemoteData.Error(IOException("error!!! $error"))
        }

        return when (result) {
            is RemoteData.Success ->
                result.output
            is RemoteData.Error -> {
                null
            }
        }
    }

    protected inline fun <reified T> getRemoteData(data: Any): T {
        return Gson().fromJson(Gson().toJson(data), T::class.java)
    }
}

sealed class RemoteData<out T : Any> {
    data class Success<out T : Any>(val output: T) : RemoteData<T>()
    data class Error(val exception: Exception) : RemoteData<Nothing>()
}