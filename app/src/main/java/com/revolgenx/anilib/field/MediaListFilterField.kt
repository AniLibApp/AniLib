package com.revolgenx.anilib.field

import android.os.Parcel
import android.os.Parcelable

class MediaListFilterField() : Parcelable {
    var search: String? = null
    var format: Int? = null
    var status: Int? = null
    var genre: String? = null

    constructor(parcel: Parcel) : this() {
        search = parcel.readString()
        format = parcel.readValue(Int::class.java.classLoader) as? Int
        status = parcel.readValue(Int::class.java.classLoader) as? Int
        genre = parcel.readString()
    }

    fun isNull() = search == null && format == null && status == null && genre == null
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(search)
        parcel.writeValue(format)
        parcel.writeValue(status)
        parcel.writeString(genre)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaListFilterField> {
        override fun createFromParcel(parcel: Parcel): MediaListFilterField {
            return MediaListFilterField(parcel)
        }

        override fun newArray(size: Int): Array<MediaListFilterField?> {
            return arrayOfNulls(size)
        }
    }
}
