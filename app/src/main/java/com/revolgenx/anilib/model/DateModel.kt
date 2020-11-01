package com.revolgenx.anilib.model

import android.os.Parcel
import android.os.Parcelable
import org.threeten.bp.LocalDate
import java.util.*

class DateModel(var year: Int? = null, var month: Int? = null, var day: Int? = null) : Parcelable {
    var date: String = ""

    init {
        date = toString()
    }

    constructor(parcel: Parcel) : this() {
        year = parcel.readValue(Int::class.java.classLoader) as? Int
        month = parcel.readValue(Int::class.java.classLoader) as? Int
        day = parcel.readValue(Int::class.java.classLoader) as? Int
        date = parcel.readString()!!
    }

    override fun toString(): String {
        return (year?.let { "$it" } ?: "") + (month?.let { "-$it" } ?: "") + (day?.let { "-$it" }
            ?: "")
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(year)
        parcel.writeValue(month)
        parcel.writeValue(day)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DateModel> {
        override fun createFromParcel(parcel: Parcel): DateModel {
            return DateModel(parcel)
        }

        override fun newArray(size: Int): Array<DateModel?> {
            return arrayOfNulls(size)
        }
    }
}


fun DateModel?.toDate(): Date {
    val c = Calendar.getInstance();
    c.set(this?.year ?: 0, this?.month ?: 0, this?.day ?: 0)
    return c.time
}