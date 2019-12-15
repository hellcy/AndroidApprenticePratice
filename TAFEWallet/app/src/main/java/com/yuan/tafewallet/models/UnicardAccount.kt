package com.yuan.tafewallet.models

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

data class UnicardAccount(var UnicardID: String, var PaperCutID: String, var QuickStreamID: String?, var Email: String,
                          var TermAndConditionAgreementAt: String?): Parcelable, Serializable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString()!!,
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(UnicardID)
        parcel.writeString(PaperCutID)
        parcel.writeString(QuickStreamID)
        parcel.writeString(Email)
        parcel.writeString(TermAndConditionAgreementAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UnicardAccount> {
        override fun createFromParcel(parcel: Parcel): UnicardAccount {
            return UnicardAccount(parcel)
        }

        override fun newArray(size: Int): Array<UnicardAccount?> {
            return arrayOfNulls(size)
        }
    }
}

class UnicardAccountManager(val context: Context) {
    fun saveUnicardAccount(unicardAccount: UnicardAccount) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        val json = Gson().toJson(unicardAccount)
        sharedPreferences.putString("UnicardAccount", json)

        sharedPreferences.apply()
    }

    fun readUnicardAccount(): UnicardAccount {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val json = sharedPreferences.getString("UnicardAccount", null)

        return Gson().fromJson(json, object: TypeToken<UnicardAccount>(){}.type)
    }

    fun savePaperCutID(paperCutID: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context).edit()
        sharedPreferences.putString("PaperCutID", paperCutID)

        sharedPreferences.apply()
    }

    fun readPaperCutID(): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        return sharedPreferences.getString("PaperCutID", null)
    }
}