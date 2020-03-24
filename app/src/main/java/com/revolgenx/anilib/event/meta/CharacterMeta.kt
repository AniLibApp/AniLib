package com.revolgenx.anilib.event.meta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterMeta(var characterId: Int, var characterUrl: String?, var siteUrl:String?) : Parcelable
