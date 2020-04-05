package com.revolgenx.anilib.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.constant.BrowseTypes
import com.revolgenx.anilib.field.browse.BrowseField
import com.revolgenx.anilib.field.browse.StaffBrowseField

class StaffBrowseFilterModel : BrowseFilterModel {
    constructor(parcel: Parcel) : super(parcel)
    constructor() : super()

    override var type: Int = BrowseTypes.STAFF.ordinal

    override fun toField(): BrowseField {
        return StaffBrowseField().also {
            it.query = query
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StaffBrowseFilterModel> {
        override fun createFromParcel(parcel: Parcel): StaffBrowseFilterModel {
            return StaffBrowseFilterModel(parcel)
        }

        override fun newArray(size: Int): Array<StaffBrowseFilterModel?> {
            return arrayOfNulls(size)
        }
    }
}