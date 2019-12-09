package com.yuan.tafewallet.models

import android.os.Parcel
import android.os.Parcelable

data class Account(var accountName: String, var accountBalance: String): Parcelable {
    constructor(source: Parcel) : this(
        source.readString()!!,
        source.readString()!!
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(accountName)
        dest.writeString(accountBalance)
    }

    companion object CREATOR: Parcelable.Creator<Account> {
        // 4
        override fun createFromParcel(source: Parcel): Account =
            Account(source)
        override fun newArray(size: Int): Array<Account?> =
            arrayOfNulls(size)
    }
}