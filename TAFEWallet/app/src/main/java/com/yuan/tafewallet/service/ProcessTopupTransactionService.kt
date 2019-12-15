package com.yuan.tafewallet.service

import com.yuan.tafewallet.models.WestpacTransaction
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ProcessTopupTransactionService {
    @POST("/v1/processTransactionByAccountToken")
    fun topupByAccountToken(@Body requestBody: TopupByAccountTokenRequestBody):
            Call<WestpacTransaction>

    @POST("/v1/processTransactionBySingleUseToken")
    fun topupBySingleUseToken(@Body requestBody: TopupBySingleUseTokenRequestBody):
            Call<WestpacTransaction>

    companion object {
        val instance: ProcessTopupTransactionService by lazy {
            val client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("Unicard_API", "1@3$"))
            val retrofit = Retrofit.Builder()
                .baseUrl("https://tafenswpayment.identityone-api.com.au")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create<ProcessTopupTransactionService>(ProcessTopupTransactionService::class.java)
        }
    }
}

class TopupByAccountTokenRequestBody(accountToken: String, amount: String, unicardID: String, paperCutID: String) {
    var AccountToken = accountToken
    var Amount = amount
    var UnicardID = unicardID
    var PaperCutID = paperCutID
}

class TopupBySingleUseTokenRequestBody(singleUseToken: String, amount: String, unicardID: String, ipAddress: String, paperCutID: String) {
    var SingleUseToken = singleUseToken
    var Amount = amount
    var UnicardID = unicardID
    var IpAddress = ipAddress
    var PaperCutID = paperCutID
}