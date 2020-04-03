package com.revolgenx.anilib.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.field.search.BaseAdvanceSearchField

abstract class BaseSearchFilterModel() : Parcelable {

    constructor(parcel: Parcel?) : this() {
        query = parcel?.readString()!!
    }

    var query: String? = null

    abstract fun    toField(): BaseAdvanceSearchField

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(query)
    }

    override fun describeContents(): Int {
        return 0
    }
}