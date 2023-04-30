package com.revolgenx.anilib.media.data.store

import androidx.datastore.core.Serializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


open class MediaFieldSerializer : Serializer<MediaFieldData> {
    override val defaultValue: MediaFieldData = MediaFieldData()

    override suspend fun readFrom(input: InputStream): MediaFieldData {
        return try {
            Json.decodeFromString(
                deserializer = MediaFieldData.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: MediaFieldData, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = MediaFieldData.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}

