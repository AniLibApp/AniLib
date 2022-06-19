package com.revolgenx.anilib.common.data.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class FuzzyDateModel(var year: Int? = null, var month: Int? = null, var day: Int? = null) : Parcelable {
    var date: String = ""

    init {
        date = toString()
    }

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
        date = parcel.readString()!!
    }

    override fun toString(): String {
        return (year?.let { "$it" } ?: "") + (month?.let { "-$it" } ?: "") + (day?.let { "-$it" }
            ?: "")
    }

    fun isEmpty() = year == null && month == null && day == null

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(year)
        parcel.writeValue(month)
        parcel.writeValue(day)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FuzzyDateModel> {
        override fun createFromParcel(parcel: Parcel): FuzzyDateModel {
            return FuzzyDateModel(parcel)
        }

        override fun newArray(size: Int): Array<FuzzyDateModel?> {
            return arrayOfNulls(size)
        }
    }
}


fun FuzzyDateModel?.toDate(): Date {
    val c = Calendar.getInstance();
    c.set(this?.year ?: 0, this?.month ?: 0, this?.day ?: 0)
    return c.time
}
