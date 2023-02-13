package com.sungbin.whatlunch_android.network.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * 서버 통신 기본 데이터 포맷
 */
data class BasicData(
    // 응답 코드 번호 [ 0: 정상 / else: 비정상 ]
    @SerializedName(value = "code")
    @Expose val code: Int,

    // 데이터 - 각 API 통신마다 해당 데이터 클래스로 매칭 시켜줘야 함
    @SerializedName(value = "data")
    @Expose val data: Any?,

    // 데이터 - 각 API 통신마다 해당 데이터 클래스로 매칭 시켜줘야 함
    @SerializedName(value = "list")
    @Expose val list: Any?,

    // 응답 메시지
    @SerializedName(value = "msg")
    @Expose val msg: String,

    // 응답 성공 여부 [ true: 성공 / false: 실패 ]
    @SerializedName(value = "success")
    @Expose val success: Boolean
)