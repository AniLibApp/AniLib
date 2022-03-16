package com.revolgenx.anilib.common.data.model

data class FuzzyDateIntModel(val year: Int, val month: Int, val day: Int) {
    fun toFuzzyDateInt() = year * 10000 + month * 100 + day
}