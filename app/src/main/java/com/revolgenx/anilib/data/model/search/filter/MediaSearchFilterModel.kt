package com.revolgenx.anilib.data.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.data.field.search.SearchField
import com.revolgenx.anilib.data.field.search.MediaSearchField

class MediaSearchFilterModel : SearchFilterModel {

    override var type: Int = SearchTypes.ANIME.ordinal
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
    var genreToExclude:List<String>? = null
    var tagsToExclude: List<String>? = null
    var hentaiOnly:Boolean   = false


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
        tagsToExclude = parcel.createStringArrayList()
        genreToExclude = parcel.createStringArrayList()
        type = parcel.readInt()
        hentaiOnly = parcel.readInt() == 1
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
        parcel.writeStringList(tagsToExclude)
        parcel.writeStringList(genreToExclude)
        parcel.writeInt(type)
        parcel.writeInt(if (hentaiOnly) 1 else 0 )
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

    override fun toField(): SearchField {
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
            it.countryOfOrigin = countryOfOrigin
            it.tags = tags
            it.genre = genre
            it.season = season
            it.type = type
            it.genreNotIn = genreToExclude
            it.tagsNotIn = tagsToExclude
            it.hentaiOnly = hentaiOnly
        }
    }

    override fun toString(): String {
        return "MediaBrowseFilterModel(season=$season, minYear=$minYear, maxYear=$maxYear, type=$type, yearEnabled=$yearEnabled, sort=$sort, format=$format, status=$status, streamingOn=$streamingOn, countryOfOrigin=$countryOfOrigin, source=$source, genre=$genre, tags=$tags)"
    }


}