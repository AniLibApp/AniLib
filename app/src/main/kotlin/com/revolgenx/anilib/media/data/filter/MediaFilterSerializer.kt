package com.revolgenx.anilib.media.data.filter

import androidx.datastore.core.Serializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


open class MediaFilterSerializer : Serializer<MediaFilter> {
    override val defaultValue: MediaFilter = MediaFilter()

    override suspend fun readFrom(input: InputStream): MediaFilter {
        return try {
            Json.decodeFromString(
                deserializer = MediaFilter.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: MediaFilter, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = MediaFilter.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}

