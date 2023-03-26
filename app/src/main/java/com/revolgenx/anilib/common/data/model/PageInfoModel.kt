package com.revolgenx.anilib.common.data.model


data class PageInfoModel(
    val perPage: Int?,
    val currentPage: Int?,
    val lastPage: Int?,
    val hasNextPage: Boolean = false,
    val total: Int?
)

