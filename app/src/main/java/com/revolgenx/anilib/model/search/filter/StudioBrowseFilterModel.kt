package com.revolgenx.anilib.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.constant.BrowseTypes
import com.revolgenx.anilib.field.browse.BrowseField
import com.revolgenx.anilib.field.browse.StudioBrowseField


class StudioBrowseFilterModel : BrowseFilterModel {
    constructor() : super()
    constructor(parcel: Parcel) : super(parcel)

    override var type: Int = BrowseTypes.STUDIO.ordinal


    override fun toField(): BrowseField {
        return StudioBrowseField().also {
            it.query = query
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudioBrowseFilterModel> {
        override fun createFromParcel(parcel: Parcel): StudioBrowseFilterModel {
            return StudioBrowseFilterModel(parcel)
        }

        override fun newArray(size: Int): Array<StudioBrowseFilterModel?> {
            return arrayOfNulls(size)
        }
    }
}