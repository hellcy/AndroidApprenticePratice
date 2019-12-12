package com.yuan.tafewallet.models

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

data class WestpacAccount(var accountType: String = "", var accountToken: String = "", var defaultAccount: Boolean = false, var cardNumber: String = "",
                             var expiryDateMonth: String = "", var expiryDateYear: String = "", var cardScheme: String = "", var cardType: String = "",
                             var cardholderName: String = "", var maskedCardNumber4Digits: String = ""):Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accountType)
        parcel.writeString(accountToken)
        parcel.writeByte(if (defaultAccount) 1 else 0)
        parcel.writeString(cardNumber)
        parcel.writeString(expiryDateMonth)
        parcel.writeString(expiryDateYear)
        parcel.writeString(cardScheme)
        parcel.writeString(cardType)
        parcel.writeString(cardholderName)
        parcel.writeString(maskedCardNumber4Digits)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WestpacAccount> {
        override fun createFromParcel(parcel: Parcel): WestpacAccount {
            return WestpacAccount(parcel)
        }

        override fun newArray(size: Int): Array<WestpacAccount?> {
            return arrayOfNulls(size)
        }
    }
}

class WestpacAccountManager(val context: Context) {
    fun saveWestpacAccount(westpacAccounts: ArrayList<WestpacAccount>) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        val json = Gson().toJson(westpacAccounts)
        sharedPreferences.putString("WestpacAccounts", json)

        sharedPreferences.apply()
    }

    fun readWestpacAccounts(): ArrayList<WestpacAccount> {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val json = sharedPreferences.getString("WestpacAccounts", "")

        return Gson().fromJson(json, object: TypeToken<ArrayList<WestpacAccount>>(){}.type)
    }
}