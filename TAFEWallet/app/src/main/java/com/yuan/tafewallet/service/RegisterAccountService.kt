package com.yuan.tafewallet.service

import com.yuan.tafewallet.models.WestpacAccount
import com.yuan.tafewallet.models.WestpacTransaction
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface RegisterAccountService {
    //@POST("/v1/registerAccountByReceiptNumber") // preprod
    @POST("/Prod/registerAccountByReceiptNumber") // test
    fun registerAccount(@Body requestBody: RegisterAccountRequestBody):
            Call<WestpacAccount>

    companion object {
        val instance: RegisterAccountService by lazy {
            val client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("Unicard_API", "1@3$"))
            val retrofit = Retrofit.Builder()
                //.baseUrl("https://tafenswpayment.identityone-api.com.au") // preprod
                .baseUrl("https://f3fcdp7la2.execute-api.ap-southeast-2.amazonaws.com") // test
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create<RegisterAccountService>(RegisterAccountService::class.java)
        }
    }
}

class RegisterAccountRequestBody(quickStreamID: String, receiptNumber: String) {
    var QuickStreamID = quickStreamID
    var ReceiptNumber = receiptNumber
}