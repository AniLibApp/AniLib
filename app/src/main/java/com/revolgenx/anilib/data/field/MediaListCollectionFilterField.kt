package com.revolgenx.anilib.data.field

import android.os.Parcel
import android.os.Parcelable

class MediaListCollectionFilterField() : Parcelable {
    var search: String? = null
    var format: Int? = null
    var status: Int? = null
    var genre: String? = null
    var listSort: Int? = null

    constructor(parcel: Parcel) : this() {
        search = parcel.readString()
        format = parcel.readValue(Int::class.java.classLoader) as? Int
        status = parcel.readValue(Int::class.java.classLoader) as? Int
        genre = parcel.readString()
        listSort = parcel.readValue(Int::class.java.classLoader) as? Int

    }

    fun isNull() =
        search == null && format == null && status == null && genre == null && listSort == null

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(search)
        parcel.writeValue(format)
        parcel.writeValue(status)
        parcel.writeString(genre)
        parcel.writeValue(listSort)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaListCollectionFilterField> {
        override fun createFromParcel(parcel: Parcel): MediaListCollectionFilterField {
            return MediaListCollectionFilterField(parcel)
        }

        override fun newArray(size: Int): Array<MediaListCollectionFilterField?> {
            return arrayOfNulls(size)
        }
    }
}
