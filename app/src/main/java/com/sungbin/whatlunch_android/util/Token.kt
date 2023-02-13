package com.sungbin.whatlunch_android.util

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class Token {
    companion object {
        @SuppressLint("StringFormatInvalid")
        fun register() {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful){
                    task.exception?.let { Log.e("Token", task.exception.toString())}
                    return@addOnCompleteListener
                }
            }
        }
    }
}