package com.revolgenx.anilib.model.home

data class HomeOrderedAdapterItem(val name: String, var order: Int, var orderType: HomeOrderType)

enum class HomeOrderType {
    AIRING, TRENDING, POPULAR, NEWLY_ADDED, WATCHING, READING
}