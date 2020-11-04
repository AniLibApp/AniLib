package com.revolgenx.anilib.data.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.data.field.search.SearchField

abstract class SearchFilterModel() : Parcelable {

    constructor(parcel: Parcel?) : this() {
        query = parcel?.readString() ?: ""
    }
    abstract var type: Int
    var query: String? = null

    abstract fun toField(): SearchField

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(query)
    }

    override fun describeContents(): Int {
        return 0
    }
}