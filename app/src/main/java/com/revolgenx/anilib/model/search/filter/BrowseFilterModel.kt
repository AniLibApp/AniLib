package com.revolgenx.anilib.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.field.browse.BrowseField

abstract class BrowseFilterModel() : Parcelable {

    constructor(parcel: Parcel?) : this() {
        query = parcel?.readString() ?: ""
    }
    abstract var type: Int
    var query: String? = null

    abstract fun toField(): BrowseField

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(query)
    }

    override fun describeContents(): Int {
        return 0
    }
}