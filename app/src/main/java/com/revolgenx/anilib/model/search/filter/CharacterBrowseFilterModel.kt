package com.revolgenx.anilib.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.constant.BrowseTypes
import com.revolgenx.anilib.field.browse.BrowseField
import com.revolgenx.anilib.field.browse.CharacterBrowseField

class CharacterBrowseFilterModel : BrowseFilterModel {

    constructor(parcel: Parcel) : super(parcel)
    constructor() : super()

    override var type: Int = BrowseTypes.CHARACTER.ordinal

    override fun toField(): BrowseField {
        return CharacterBrowseField().also {
            it.query = query
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CharacterBrowseFilterModel> {
        override fun createFromParcel(parcel: Parcel): CharacterBrowseFilterModel {
            return CharacterBrowseFilterModel(parcel)
        }

        override fun newArray(size: Int): Array<CharacterBrowseFilterModel?> {
            return arrayOfNulls(size)
        }
    }
}