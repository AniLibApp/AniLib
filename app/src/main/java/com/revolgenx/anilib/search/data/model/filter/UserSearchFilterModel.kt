package com.revolgenx.anilib.search.data.model.filter

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.constant.SearchTypes
import com.revolgenx.anilib.search.data.field.SearchField
import com.revolgenx.anilib.search.data.field.UserSearchField

class UserSearchFilterModel : SearchFilterModel {
    override var type: Int = SearchTypes.USER.ordinal

    constructor(parcel: Parcel) : super(parcel)
    constructor() : super()

    override fun toField(): SearchField {
        return UserSearchField().also {
            it.query = query
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserSearchFilterModel> {
        override fun createFromParcel(parcel: Parcel): UserSearchFilterModel {
            return UserSearchFilterModel(parcel)
        }

        override fun newArray(size: Int): Array<UserSearchFilterModel?> {
            return arrayOfNulls(size)
        }
    }
}