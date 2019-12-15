package com.yuan.tafewallet.models

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.MotionEvent
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

data class WestpacTransaction(var transactionType: String = "", var transactionTime: String = "", var status: String = "", var receiptNumber: String = "",
                              var totalAmount: Money = Money(), var creditCard: CreditCard = CreditCard(), var refundableAmount: String? = "", var updatedBalance: String? = "")
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

data class Money(var currency: String = "", var amount: Double = 0.0): Serializable, Parcelable {
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

data class CreditCard(var cardScheme: String = "", var cardType: String = "", var cardNumber: String = "", var cardholderName: String = "",
                      var expiryDateMonth: String = "", var expiryDateYear: String = "", var accountToken: String? = ""): Serializable, Parcelable {
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

class WestpacTransactionManager(val context: Context) {
    fun saveWestpacTransactions(westpacTransactions: ArrayList<WestpacTransaction>) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        val json = Gson().toJson(westpacTransactions)
        sharedPreferences.putString("WestpacTransaction", json)

        sharedPreferences.apply()
    }

    fun readWestpacTransactions(): ArrayList<WestpacTransaction> {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val json = sharedPreferences.getString("WestpacTransaction", "")

        return Gson().fromJson(json, object : TypeToken<ArrayList<WestpacTransaction>>() {}.type)
    }
}