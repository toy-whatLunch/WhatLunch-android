package com.sungbin.whatlunch_android.network.data

data class MyUser(
    val birthDate: String?= null,
    val createDate: String,
    val deviceInfo: DeviceInfo?= null,
    val email: String,
    val gender: String?= null,
    val memberKey: Int,
    val name: String,
    val phone: String?= null,
    val uid: String,
    val updateDate: String?= null
) {
    data class DeviceInfo(
        val createDate: String,
        val fcmToken: String,
        val updateDate: String? = null
    )
}