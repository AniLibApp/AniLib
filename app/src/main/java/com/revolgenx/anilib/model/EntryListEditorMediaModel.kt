package com.revolgenx.anilib.model

import android.os.Parcel
import android.os.Parcelable
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType

class EntryListEditorMediaModel() : Parcelable {
    var mediaId:Int = -1
    var listId: Int = -1
    var userId: Int = -1
    var type: Int = MediaType.ANIME.ordinal
    var status: Int = MediaListStatus.CURRENT.ordinal
    var score: Double = 0.0
    var progress: Int = 0
    var progressVolumes: Int = 0
    var repeat: Int = 0
    var notes: String = ""
    var private: Boolean = false
    var startDate: DateModel? = null
    var endDate: DateModel? = null

    constructor(parcel: Parcel) : this() {
        listId = parcel.readInt()
        userId = parcel.readInt()
        type = parcel.readInt()
        status = parcel.readInt()
        score = parcel.readDouble()
        progress = parcel.readInt()
        repeat = parcel.readInt()
        notes = parcel.readString()!!
        private = parcel.readByte() != 0.toByte()
        startDate = parcel.readParcelable(DateModel::class.java.classLoader)
        endDate = parcel.readParcelable(DateModel::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(listId)
        parcel.writeInt(userId)
        parcel.writeInt(type)
        parcel.writeInt(status)
        parcel.writeDouble(score)
        parcel.writeInt(progress)
        parcel.writeInt(repeat)
        parcel.writeString(notes)
        parcel.writeByte(if (private) 1 else 0)
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