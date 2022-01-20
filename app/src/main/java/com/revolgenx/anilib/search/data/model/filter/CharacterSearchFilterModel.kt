package com.revolgenx.anilib.search.data.model.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.search.data.field.CharacterSearchField
import com.revolgenx.anilib.search.data.field.SearchField

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