package com.revolgenx.anilib.search.data.model.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.search.data.field.SearchField
import com.revolgenx.anilib.search.data.field.StudioSearchField

class StudioSearchFilterModel : SearchFilterModel {
    constructor() : super()
    constructor(parcel: Parcel) : super(parcel)

    override var type: Int = SearchTypes.STUDIO.ordinal


    override fun toField(): SearchField {
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