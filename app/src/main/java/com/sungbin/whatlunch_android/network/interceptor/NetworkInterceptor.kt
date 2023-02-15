package com.sungbin.whatlunch_android.network.interceptor

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.util.concurrent.TimeUnit

class NetworkInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val user = FirebaseAuth.getInstance().currentUser

        if(user == null){
            val builder: Request.Builder = chain.request().newBuilder()
            builder.addHeader("User-Agent", "aos")

            return chain.proceed(builder.build())
        }else{
            val result = kotlin.runCatching {
                val task = user.getIdToken(false)

                val token = Tasks.await(task, 30, TimeUnit.SECONDS)
                token.token?:return chain.proceed(request)
            }

            return if(result.isSuccess){
                val token = result.getOrNull()
                val builder: Request.Builder = chain.request().newBuilder()
                builder.addHeader("User-Agent", "aos")
                token?.let { token ->
                    builder.addHeader("F-AUTH-TOKEN", token)
                }

                chain.proceed(builder.build())
            }else{
                chain.proceed(request)
            }
        }

    }
}