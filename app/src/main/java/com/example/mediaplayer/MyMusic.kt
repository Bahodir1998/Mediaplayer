package com.example.mediaplayer

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class MyMusic(
    val uri: Uri,
    val artist: String,
    val title: String,
    val duration: Int,
    val albumId: Long
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Uri::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(uri, flags)
        parcel.writeString(artist)
        parcel.writeString(title)
        parcel.writeInt(duration)
        parcel.writeLong(albumId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyMusic> {
        override fun createFromParcel(parcel: Parcel): MyMusic {
            return MyMusic(parcel)
        }

        override fun newArray(size: Int): Array<MyMusic?> {
            return arrayOfNulls(size)
        }
    }

}