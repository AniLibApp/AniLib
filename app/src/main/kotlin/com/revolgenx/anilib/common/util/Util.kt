package com.revolgenx.anilib.common.util

import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.revolgenx.anilib.BuildConfig


fun immutablePendingFlag(flag: Int) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
    flag or PendingIntent.FLAG_IMMUTABLE
else flag

fun mutablePendingFlag(flag: Int) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
    flag or PendingIntent.FLAG_MUTABLE
else flag

val immutableFlagUpdateCurrent get() = immutablePendingFlag(PendingIntent.FLAG_UPDATE_CURRENT)
val immutableFlagEmpty get() = immutablePendingFlag(0)


val versionName = "${BuildConfig.VERSION_NAME}-${BuildConfig.VERSION_CODE}"