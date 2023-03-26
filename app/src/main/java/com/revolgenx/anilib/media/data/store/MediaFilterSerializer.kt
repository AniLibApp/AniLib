package com.revolgenx.anilib.media.data.store

import androidx.datastore.core.Serializer
import com.revolgenx.anilib.media.data.model.MediaFilterModel
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

class MediaFilterSerializer: Serializer<MediaFilterModel> {
    override val defaultValue: MediaFilterModel = MediaFilterModel.default()

    override suspend fun readFrom(input: InputStream): MediaFilterModel {
        return try {
            Json.decodeFromString(
                deserializer = MediaFilterModel.serializer(),
                string = input.readBytes().decodeToString()
            )
        }catch (e:Exception){
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: MediaFilterModel, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = MediaFilterModel.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}