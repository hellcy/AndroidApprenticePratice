package com.yuan.tafewallet.service

import com.yuan.tafewallet.models.UnicardAccount
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface GenerateUnicardIDService {
    // 1
    @POST("/TAFEOnsiteAPIForMobileAndWebPortal/api/TAFEOnsiteAPI/RegisterUserToUnicardSQLDatabase")
    // 2
    fun generateUnicardID(@Body requestBody: GenerateUnicardIDRequestBody):
            Call<ArrayList<UnicardAccount>>
    // 3
    companion object {
        // 4
        val instance: GenerateUnicardIDService by lazy {
            // 5
            val client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("Unicard_API", "1@3$"))
            val retrofit = Retrofit.Builder()
                .baseUrl("https://idmobile-dev.com.au")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // 6
            retrofit.create<GenerateUnicardIDService>(GenerateUnicardIDService::class.java)
        }
    }
}

class GenerateUnicardIDRequestBody(unicardID: String, paperCutID: String, quickStreamID: String, emailAddress: String, termAndConditionTimeStamp: String) {
    var UnicardID = unicardID
    var PaperCutID = paperCutID
    var QuickStreamID = quickStreamID
    var EmailAddress = emailAddress
    var TermAndConditionTimeStamp = termAndConditionTimeStamp
}