package com.revolgenx.anilib.list.constant

import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType

object ListConstant {
    fun statusToDefaultGroup(status:MediaListStatus, type:MediaType): String {
        return when(status){
            MediaListStatus.CURRENT -> if(type == MediaType.ANIME) "Watching" else "Reading"
            MediaListStatus.PLANNING -> "Planning"
            MediaListStatus.COMPLETED -> "Completed"
            MediaListStatus.DROPPED -> "Dropped"
            MediaListStatus.PAUSED -> "Paused"
            MediaListStatus.REPEATING -> if(type == MediaType.ANIME) "Rewatching" else "Rereading"
            else->""
        }
    }
}