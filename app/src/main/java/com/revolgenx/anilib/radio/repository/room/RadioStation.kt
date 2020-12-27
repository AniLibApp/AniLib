package com.revolgenx.anilib.radio.repository.room

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.revolgenx.anilib.radio.data.PlaybackState
import java.util.*

@TypeConverters(RadioChannelConverter::class, DbDateConverter::class)
@Entity(tableName = "RadioStations")
data class RadioStation(
    @PrimaryKey(autoGenerate = false) @ColumnInfo(name = "id") val id: Long = 0,
    val name: String,
    val site: String,
    val logo: String?,
    var isFavourite: Boolean = false,
    var backgroundColor: String? = null,
    var channel: List<RadioChannel>
) : RadioStationAudit() {
    @Ignore
    var streamTitle: String? = null

    @Ignore
    var playbackState: PlaybackState = PlaybackState.RadioStopState(null)

    @Ignore
    var playbackStateListener: ((state: PlaybackState) -> Unit)? = null
}


data class RadioChannel(
    val stream: String,
    val bitrate: Int,
    val default: Boolean
)


abstract class RadioStationAudit {
    @ColumnInfo(name = "accessedOn")
    var accessedOn: Date? = null
}


class RadioChannelConverter {
    private val typeToken = object : TypeToken<List<RadioChannel>>() {}.type

    @TypeConverter
    fun fromList(radioChannel: List<RadioChannel>): String? {
        return Gson().toJson(radioChannel)
    }

    @TypeConverter
    fun toList(radioChannel: String): List<RadioChannel>? {
        return Gson().fromJson(radioChannel, typeToken)
    }

}

class DbDateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}


