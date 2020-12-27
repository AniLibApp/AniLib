package com.revolgenx.anilib.radio.repository.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RadioStation(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "site") val site: String,
    @field:Json(name = "logo") val logo: String?,
    @field:Json(name = "isObsolete") val isObsolete: Boolean?,
    @field:Json(name = "channel") val channel: List<RadioChannel>,
)


@JsonClass(generateAdapter = true)
data class RadioChannel(
    @field:Json(name = "stream") val stream: String,
    @field:Json(name = "bitrate") val bitrate: Int,
    @field:Json(name = "default") val default: Boolean,
)