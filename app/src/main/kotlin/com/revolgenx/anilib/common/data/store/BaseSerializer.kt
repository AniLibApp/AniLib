package com.revolgenx.anilib.common.data.store

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream


abstract class BaseSerializer<T>(override val defaultValue: T) : Serializer<T> {
    abstract fun serializer(): KSerializer<T>

    override suspend fun readFrom(input: InputStream): T {
        return try {
            Json.decodeFromString(
                deserializer = serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: T, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = serializer(),
                    value = t
                ).encodeToByteArray()
            )
        }
    }
}

