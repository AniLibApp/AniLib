package com.revolgenx.anilib.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.field.search.BrowseField
import com.revolgenx.anilib.field.search.StudioSearchField


class StudioSearchFilterModel : BaseSearchFilterModel {
    constructor() : super()
    constructor(parcel: Parcel) : super(parcel)

    override fun toField(): BrowseField {
        return StudioSearchField().also {
            it.query = query
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudioSearchFilterModel> {
        override fun createFromParcel(parcel: Parcel): StudioSearchFilterModel {
            return StudioSearchFilterModel(parcel)
        }

        override fun newArray(size: Int): Array<StudioSearchFilterModel?> {
            return arrayOfNulls(size)
        }
    }
}