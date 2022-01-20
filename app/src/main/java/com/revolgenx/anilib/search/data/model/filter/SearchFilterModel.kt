package com.revolgenx.anilib.search.data.model.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.search.data.field.SearchField

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