package com.revolgenx.anilib.common.ui.model

import com.revolgenx.anilib.fragment.FuzzyDate


data class FuzzyDateModel(val day:Int?, val month: Int?, val year:Int?){
    override fun toString(): String {
        return "${year ?: ""}-${month ?: ""}-${day ?: ""}"
    }
}

fun FuzzyDate.toModel() = FuzzyDateModel(
    day,
    month,
    year
).takeIf { it.year != null || it.month != null || it.day != null }