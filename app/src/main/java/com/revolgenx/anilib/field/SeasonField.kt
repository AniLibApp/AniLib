package com.revolgenx.anilib.field

import android.content.Context
import com.revolgenx.anilib.SeasonListQuery
import com.revolgenx.anilib.field.media.MediaField
import com.revolgenx.anilib.preference.getSeasonField
import com.revolgenx.anilib.type.MediaFormat
import com.revolgenx.anilib.type.MediaSeason
import com.revolgenx.anilib.type.MediaStatus


class SeasonField : MediaField() {
    companion object {
        fun create(context: Context) = getSeasonField(context)
    }


//    val sorted:String
//        get() {
//            return if (order == "ASC") sort else sort + "_" + order
//        }


//    fun changeSeason(i: Int) {
//        season += i
//        if (season > 3) {
//            year += 1
//            season = 0
//        } else if (season < 0) {
//            year -= 1
//            season = 3
//        }
//    }

//    fun update(context: Context) {
//        setSeasonField(context, this)
//    }

}