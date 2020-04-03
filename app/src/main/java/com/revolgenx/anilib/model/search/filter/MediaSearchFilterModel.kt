package com.revolgenx.anilib.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.revolgenx.anilib.constant.AdvanceSearchTypes
import com.revolgenx.anilib.field.search.BaseAdvanceSearchField
import com.revolgenx.anilib.field.search.MediaSearchField

class MediaSearchFilterModel : BaseSearchFilterModel {

    var season: Int? = null
    var minYear: Int? = null
    var maxYear: Int? = null
    var type: Int? = null
    var yearEnabled = false
    var isYearSame = minYear == maxYear

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
        format = parcel.readValue(Int::class.java.classLoader) as? Int
        status = parcel.readValue(Int::class.java.classLoader) as? Int
        streamingOn = parcel.createStringArrayList()
        countryOfOrigin = parcel.readValue(Int::class.java.classLoader) as? Int
        source = parcel.readValue(Int::class.java.classLoader) as? Int
        genre = parcel.createStringArrayList()
        tags = parcel.createStringArrayList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeValue(season)
        parcel.writeValue(minYear)
        parcel.writeValue(maxYear)
        parcel.writeValue(format)
        parcel.writeValue(status)
        parcel.writeStringList(streamingOn)
        parcel.writeValue(countryOfOrigin)
        parcel.writeValue(source)
        parcel.writeStringList(genre)
        parcel.writeStringList(tags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaSearchFilterModel> {
        override fun createFromParcel(parcel: Parcel): MediaSearchFilterModel {
            return MediaSearchFilterModel(
                parcel
            )
        }

        override fun newArray(size: Int): Array<MediaSearchFilterModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun toField(): BaseAdvanceSearchField {
        return MediaSearchField().also {
            it.query = query
            it.minYear = minYear
            it.maxYear = maxYear
            it.yearEnabled = yearEnabled
            it.sort = sort
            it.format = format
            it.status = status
            it.source = source
            it.streamingOn = streamingOn
            it.tags = tags
            it.genre = genre
            it.type = type?:it.type
        }
    }
}