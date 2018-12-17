package com.ctd.cymanage.bean

import android.os.Parcel
import android.os.Parcelable

data class MakerInfo(

    var latitude: Double,
    var longitude: Double,
    var title: String,
    var status: Int,
    var num: Int,
    var imgPath: String = "https://gss2.bdstatic.com/-fo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=2aad344f33fa828bc52e95b19c762a51/060828381f30e9242e0a2c7b4c086e061c95f7ca.jpg"

) : Parcelable {
    constructor(source: Parcel) : this(
        source.readDouble(),
        source.readDouble(),
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeDouble(latitude)
        writeDouble(longitude)
        writeString(title)
        writeInt(status)
        writeInt(num)
        writeString(imgPath)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MakerInfo> = object : Parcelable.Creator<MakerInfo> {
            override fun createFromParcel(source: Parcel): MakerInfo = MakerInfo(source)
            override fun newArray(size: Int): Array<MakerInfo?> = arrayOfNulls(size)
        }
    }
}