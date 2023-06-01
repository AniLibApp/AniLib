package com.revolgenx.anilib.common.ui.model

import androidx.annotation.StringRes

data class HeaderModel(val title: String? = null, @StringRes val titleRes: Int? = null) :
    BaseModel(title ?: titleRes!!)