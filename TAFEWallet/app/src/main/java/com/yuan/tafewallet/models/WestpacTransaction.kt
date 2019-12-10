package com.yuan.tafewallet.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class WestpacTransaction(var transactionType: String, var transactionTime: String, var status: String, var receiptNumber: String,
                              var totalAmount: Money, var creditCard: CreditCard, var refundableAmount: String?, var updatedBalance: String?)
    : Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Money::class.java.classLoader)!!,
        parcel.readParcelable(CreditCard::class.java.classLoader)!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(transactionType)
        parcel.writeString(transactionTime)
        parcel.writeString(status)
        parcel.writeString(receiptNumber)
        parcel.writeParcelable(totalAmount, flags)
        parcel.writeParcelable(creditCard, flags)
        parcel.writeString(refundableAmount)
        parcel.writeString(updatedBalance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WestpacTransaction> {
        override fun createFromParcel(parcel: Parcel): WestpacTransaction {
            return WestpacTransaction(parcel)
        }

        override fun newArray(size: Int): Array<WestpacTransaction?> {
            return arrayOfNulls(size)
        }
    }
}

data class Money(var currency: String, var amount: Double): Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(currency)
        parcel.writeDouble(amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Money> {
        override fun createFromParcel(parcel: Parcel): Money {
            return Money(parcel)
        }

        override fun newArray(size: Int): Array<Money?> {
            return arrayOfNulls(size)
        }
    }
}

data class CreditCard(var cardScheme: String, var cardType: String, var cardNumber: String, var cardholderName: String,
                      var expiryDateMonth: String, var expiryDateYear: String, var accountToken: String): Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cardScheme)
        parcel.writeString(cardType)
        parcel.writeString(cardNumber)
        parcel.writeString(cardholderName)
        parcel.writeString(expiryDateMonth)
        parcel.writeString(expiryDateYear)
        parcel.writeString(accountToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CreditCard> {
        override fun createFromParcel(parcel: Parcel): CreditCard {
            return CreditCard(parcel)
        }

        override fun newArray(size: Int): Array<CreditCard?> {
            return arrayOfNulls(size)
        }
    }
}