package com.revolgenx.anilib.data.model.search.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.data.field.search.SearchField
import com.revolgenx.anilib.data.field.search.CharacterSearchField

class CharacterSearchFilterModel : SearchFilterModel {

    constructor(parcel: Parcel) : super(parcel)
    constructor() : super()

    override var type: Int = SearchTypes.CHARACTER.ordinal

    override fun toField(): SearchField {
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