package com.sungbin.whatlunch_android.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object Util {
    /**
     * 앱 내 사용 권한 허용 여부 검사
     *
     * true: 모든 권한 허용
     * false: 하나 이상 권한 거부
     */
    fun checkPermission(vararg permissions: String, context: Context): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}