package com.revolgenx.anilib.data.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.data.field.search.SearchField
import com.revolgenx.anilib.data.field.search.StaffSearchField

class StaffSearchFilterModel : SearchFilterModel {
    constructor(parcel: Parcel) : super(parcel)
    constructor() : super()

    override var type: Int = SearchTypes.STAFF.ordinal

    override fun toField(): SearchField {
        return StaffSearchField().also {
            it.query = query
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StaffSearchFilterModel> {
        override fun createFromParcel(parcel: Parcel): StaffSearchFilterModel {
            return StaffSearchFilterModel(parcel)
        }

        override fun newArray(size: Int): Array<StaffSearchFilterModel?> {
            return arrayOfNulls(size)
        }
    }
}