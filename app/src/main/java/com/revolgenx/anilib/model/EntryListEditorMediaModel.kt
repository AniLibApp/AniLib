package com.revolgenx.anilib.model

import android.os.Parcel
import android.os.Parcelable

class EntryListEditorMediaModel() : Parcelable {
    var mediaId: Int? = null
    var listId: Int? = null
    var userId: Int? = null
    var type: Int? = null
    var status: Int? = null
    var score: Double? = null
    var progress: Int? = null
    var progressVolumes: Int? = null
    var repeat: Int? = null
    var notes: String? = null
    var private: Boolean? = null
    var startDate: DateModel? = null
    var endDate: DateModel? = null

    constructor(parcel: Parcel) : this() {
        mediaId = parcel.readValue(Int::class.java.classLoader) as? Int
        listId = parcel.readValue(Int::class.java.classLoader) as? Int
        userId = parcel.readValue(Int::class.java.classLoader) as? Int
        type = parcel.readValue(Int::class.java.classLoader) as? Int
        status = parcel.readValue(Int::class.java.classLoader) as? Int
        score = parcel.readValue(Double::class.java.classLoader) as? Double
        progress = parcel.readValue(Int::class.java.classLoader) as? Int
        progressVolumes = parcel.readValue(Int::class.java.classLoader) as? Int
        repeat = parcel.readValue(Int::class.java.classLoader) as? Int
        notes = parcel.readString()
        private = parcel.readValue(Boolean::class.java.classLoader) as? Boolean
        startDate = parcel.readParcelable(DateModel::class.java.classLoader)
        endDate = parcel.readParcelable(DateModel::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(mediaId)
        parcel.writeValue(listId)
        parcel.writeValue(userId)
        parcel.writeValue(type)
        parcel.writeValue(status)
        parcel.writeValue(score)
        parcel.writeValue(progress)
        parcel.writeValue(progressVolumes)
        parcel.writeValue(repeat)
        parcel.writeString(notes)
        parcel.writeValue(private)
        parcel.writeParcelable(startDate, flags)
        parcel.writeParcelable(endDate, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntryListEditorMediaModel> {
        override fun createFromParcel(parcel: Parcel): EntryListEditorMediaModel {
            return EntryListEditorMediaModel(parcel)
        }

        override fun newArray(size: Int): Array<EntryListEditorMediaModel?> {
            return arrayOfNulls(size)
        }
    }

}