package com.revolgenx.anilib.app.theme

import android.graphics.Color

object AlThemes {
    fun getThemes() = listOf(
        AlThemeModel("Bee", 2, Color.parseColor("#000000"), -3, Color.parseColor("#FFEB3B"))
    )
}

data class AlThemeModel(
    val name: String,
    val index: Int,
    val backgroundColor: Int,
    val surfaceColor: Int,
    val accentColor: Int
)

