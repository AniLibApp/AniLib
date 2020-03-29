package com.revolgenx.anilib

import com.revolgenx.anilib.type.MediaSort
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    val str = listOf(   "Title Romaji",
        "Title Romaji Desc",
        "Title English",
        "Title English Desc",
        "Title Native",
        "Title Native Desc",
        "Type",
        "Type Desc",
        "Format",
        "Format Desc",
        "Start Date",
        "Start Date Desc",
        "End Date",
        "End Date Desc",
        "Score",
        "Score Desc",
        "Popularity",
        "Popularity Desc",
        "Trending",
        "Trending Desc",
        "Episodes",
        "Episodes Desc",
        "Duration",
        "Duration Desc",
        "Status",
        "Status Desc",
        "Chapters",
        "Chapters Desc",
        "Volumes",
        "Volumes Desc",
        "Updated At",
        "Updated At Desc",
        "Favourites",
        "Favourites Desc")

    @Test
    fun testSort(){

        str.map { it.replace(" ", "_").toUpperCase() }.forEach {
            println(MediaSort.valueOf(it))
        }
    }
}
