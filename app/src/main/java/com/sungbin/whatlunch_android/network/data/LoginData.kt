package com.sungbin.whatlunch_android.network.data

data class LoginData(
    val email: String,
    val name: String,
    val provider: String,
    val uid: String
)