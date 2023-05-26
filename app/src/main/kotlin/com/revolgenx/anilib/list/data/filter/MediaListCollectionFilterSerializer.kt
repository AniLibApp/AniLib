package com.revolgenx.anilib.list.data.filter

import androidx.datastore.core.Serializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class MediaListCollectionFilterSerializer : Serializer<MediaListCollectionFilter> {
    override val defaultValue: MediaListCollectionFilter = MediaListCollectionFilter()

    override suspend fun readFrom(input: InputStream): MediaListCollectionFilter {
        return try {
            Json.decodeFromString(
                deserializer = MediaListCollectionFilter.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: MediaListCollectionFilter, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = MediaListCollectionFilter.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}

