package com.revolgenx.anilib.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.constant.BrowseTypes
import com.revolgenx.anilib.field.browse.BrowseField
import com.revolgenx.anilib.field.browse.MediaBrowseField

class MediaBrowseFilterModel : BrowseFilterModel {

    override var type: Int = BrowseTypes.ANIME.ordinal
    var season: Int? = null
    var minYear: Int? = null
    var maxYear: Int? = null
    var yearEnabled = false
    var sort: Int? = null
    var format: Int? = null
    var status: Int? = null
    var streamingOn: List<String>? = null
    var countryOfOrigin: Int? = null
    var source: Int? = null
    var genre: List<String>? = null
    var tags: List<String>? = null

    constructor() : super()

    constructor(parcel: Parcel) : super(parcel) {
        season = parcel.readValue(Int::class.java.classLoader) as? Int
        minYear = parcel.readValue(Int::class.java.classLoader) as? Int
        maxYear = parcel.readValue(Int::class.java.classLoader) as? Int
        yearEnabled = parcel.readInt() == 1
        sort = parcel.readValue(Int::class.java.classLoader) as? Int
        format = parcel.readValue(Int::class.java.classLoader) as? Int
        status = parcel.readValue(Int::class.java.classLoader) as? Int
        streamingOn = parcel.createStringArrayList()
        countryOfOrigin = parcel.readValue(Int::class.java.classLoader) as? Int
        source = parcel.readValue(Int::class.java.classLoader) as? Int
        genre = parcel.createStringArrayList()
        tags = parcel.createStringArrayList()
        type = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeValue(season)
        parcel.writeValue(minYear)
        parcel.writeValue(maxYear)
        parcel.writeInt(if (yearEnabled) 1 else 0)
        parcel.writeValue(sort)
        parcel.writeValue(format)
        parcel.writeValue(status)
        parcel.writeStringList(streamingOn)
        parcel.writeValue(countryOfOrigin)
        parcel.writeValue(source)
        parcel.writeStringList(genre)
        parcel.writeStringList(tags)
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaBrowseFilterModel> {
        override fun createFromParcel(parcel: Parcel): MediaBrowseFilterModel {
            return MediaBrowseFilterModel(
                parcel
            )
        }

        override fun newArray(size: Int): Array<MediaBrowseFilterModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun toField(): BrowseField {
        return MediaBrowseField().also {
            it.query = query
            it.minYear = minYear
            it.maxYear = maxYear
            it.yearEnabled = yearEnabled
            it.sort = sort
            it.format = format
            it.status = status
            it.source = source
            it.streamingOn = streamingOn
            it.countryOfOrigin = countryOfOrigin
            it.tags = tags
            it.genre = genre
            it.season = season
            it.type = type
        }
    }

    override fun toString(): String {
        return "MediaBrowseFilterModel(season=$season, minYear=$minYear, maxYear=$maxYear, type=$type, yearEnabled=$yearEnabled, sort=$sort, format=$format, status=$status, streamingOn=$streamingOn, countryOfOrigin=$countryOfOrigin, source=$source, genre=$genre, tags=$tags)"
    }


}