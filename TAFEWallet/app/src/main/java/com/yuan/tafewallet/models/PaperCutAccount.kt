package com.yuan.tafewallet.models

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

data class PaperCutAccount(var FullName: String?, var UserName: String?, var Balance: Double, var AccountName: String?,
                           var AccountType: String?, var LastActivityDate: String?, var Email: String?): Serializable, Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(FullName)
        parcel.writeString(UserName)
        parcel.writeDouble(Balance)
        parcel.writeString(AccountName)
        parcel.writeString(AccountType)
        parcel.writeString(LastActivityDate)
        parcel.writeString(Email)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaperCutAccount> {
        override fun createFromParcel(parcel: Parcel): PaperCutAccount {
            return PaperCutAccount(parcel)
        }

        override fun newArray(size: Int): Array<PaperCutAccount?> {
            return arrayOfNulls(size)
        }
    }
}

class PaperCutAccountManager(val context: Context) {
    fun savePaperCutAccount(paperCutAccounts: ArrayList<PaperCutAccount>) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        val json = Gson().toJson(paperCutAccounts)
        sharedPreferences.putString("PaperCutAccount", json)

        sharedPreferences.apply()
    }

    fun readPaperCutAccounts(): ArrayList<PaperCutAccount>? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val json = sharedPreferences.getString("PaperCutAccount", null)
        if (json == null) return null
        else return Gson().fromJson(json, object: TypeToken<ArrayList<PaperCutAccount>>(){}.type)
    }

    fun savePrimaryAccount(paperCutAccounts: ArrayList<PaperCutAccount>) {
        for (account in paperCutAccounts) {
            if (account.AccountType == "USER") {
                val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
                val json = Gson().toJson(account)
                sharedPreferences.putString("PrimaryAccount", json)

                sharedPreferences.apply()
            }
        }
    }

    fun readPrimaryAccount(): PaperCutAccount {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val json = sharedPreferences.getString("PrimaryAccount", "")

        return Gson().fromJson(json, object: TypeToken<PaperCutAccount>(){}.type)
    }
}