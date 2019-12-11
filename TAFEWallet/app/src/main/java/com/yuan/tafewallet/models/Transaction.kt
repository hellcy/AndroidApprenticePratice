package com.yuan.tafewallet.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Transaction(var TransactionType: String, var Comments: String?, var amount: Double,
                       var TransactionDate: String, var is_credit: String): Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(TransactionType)
        parcel.writeString(Comments)
        parcel.writeDouble(amount)
        parcel.writeString(TransactionDate)
        parcel.writeString(is_credit)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }

}