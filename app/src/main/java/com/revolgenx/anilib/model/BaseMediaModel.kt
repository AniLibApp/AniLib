package com.revolgenx.anilib.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

open class BaseMediaModel() :Parcelable{
    var id:Int = -1

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BaseMediaModel> {
        override fun createFromParcel(parcel: Parcel): BaseMediaModel {
            return BaseMediaModel(parcel)
        }

        override fun newArray(size: Int): Array<BaseMediaModel?> {
            return arrayOfNulls(size)
        }
    }
}