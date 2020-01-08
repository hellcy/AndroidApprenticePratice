package com.yuan.tafewallet.service

import com.yuan.tafewallet.models.UnicardAccount
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UpdateUnicardAccountService {
    // 1
    //@POST("/v1/updateUnicardAccount") // preprod
    @POST("/Prod/updateUnicardAccount") // test
    // 2
    fun updateUnicardAccount(@Body requestBody: UpdateUnicardAccountRequestBody):
            Call<ArrayList<UnicardAccount>>
    // 3
    companion object {
        // 4
        val instance: UpdateUnicardAccountService by lazy {
            // 5
            val client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("Unicard_API", "1@3$"))
            val retrofit = Retrofit.Builder()
                //.baseUrl("https://tafenswpayment.identityone-api.com.au") // preprod
                .baseUrl("https://f3fcdp7la2.execute-api.ap-southeast-2.amazonaws.com") // test
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // 6
            retrofit.create<UpdateUnicardAccountService>(UpdateUnicardAccountService::class.java)
        }
    }
}

class UpdateUnicardAccountRequestBody(unicardID: String, paperCutID: String, quickStreamID: String, emailAddress: String, termAndConditionTimeStamp: String) {
    var UnicardID = unicardID
    var PaperCutID = paperCutID
    var QuickStreamID = quickStreamID
    var EmailAddress = emailAddress
    var TermAndConditionTimeStamp = termAndConditionTimeStamp
}