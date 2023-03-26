package com.revolgenx.anilib.common.ext

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.revolgenx.anilib.R

fun String?.naText() = this.takeIf { !it.isNullOrEmpty() } ?: "?"
fun Int?.naText() = this.takeIf { it != null }?.toString() ?: "?"

@StringRes
fun Int?.naStringRes() = this.takeIf { it != null } ?: R.string.na
@DrawableRes
fun Int?.naDrawableRes() = this.takeIf { it != null } ?: R.drawable.ic_question_mark

