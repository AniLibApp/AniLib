package com.revolgenx.anilib.thread.ui.model

import com.revolgenx.anilib.common.ui.model.BaseModel


data class ThreadModel(
    val id: Int,
    val title: String?,
    val siteUrl: String?
) : BaseModel(id)