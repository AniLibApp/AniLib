package com.revolgenx.anilib.event.meta

import android.os.Parcel
import android.os.Parcelable
open class ListEditorMeta(
    var id: Int,
    var title: String,
    var coverImage: String,
    var bannerImage: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(coverImage)
        parcel.writeString(bannerImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListEditorMeta> {
        override fun createFromParcel(parcel: Parcel): ListEditorMeta {
            return ListEditorMeta(parcel)
        }

        override fun newArray(size: Int): Array<ListEditorMeta?> {
            return arrayOfNulls(size)
        }
    }

}