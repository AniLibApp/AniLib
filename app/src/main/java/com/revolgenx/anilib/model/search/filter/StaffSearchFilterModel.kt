package com.revolgenx.anilib.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.field.search.BaseAdvanceSearchField
import com.revolgenx.anilib.field.search.StaffSearchField

class StaffSearchFilterModel(parcel: Parcel?) : BaseSearchFilterModel(parcel) {

    constructor():this(null)
    override fun toField(): BaseAdvanceSearchField {
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