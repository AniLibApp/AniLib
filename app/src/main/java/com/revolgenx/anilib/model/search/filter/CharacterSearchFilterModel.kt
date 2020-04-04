package com.revolgenx.anilib.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.field.search.BrowseField
import com.revolgenx.anilib.field.search.CharacterSearchField

class CharacterSearchFilterModel : BaseSearchFilterModel {

    constructor(parcel: Parcel) : super(parcel)
    constructor() : super()

    override fun toField(): BrowseField {
        return CharacterSearchField().also {
            it.query = query
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CharacterSearchFilterModel> {
        override fun createFromParcel(parcel: Parcel): CharacterSearchFilterModel {
            return CharacterSearchFilterModel(parcel)
        }

        override fun newArray(size: Int): Array<CharacterSearchFilterModel?> {
            return arrayOfNulls(size)
        }
    }
}