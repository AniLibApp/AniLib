package com.revolgenx.anilib.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaTagsModel(
    val name:String,
    val description:String?,
    val category:String?,
    val isMediaSpoilerTag:Boolean,
    val rank:Int?,
    val isAdult:Boolean?
):Parcelable
