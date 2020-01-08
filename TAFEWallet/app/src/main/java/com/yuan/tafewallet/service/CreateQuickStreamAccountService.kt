package com.yuan.tafewallet.service

import com.yuan.tafewallet.models.PaperCutAccount
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateQuickStreamAccountService {
    // 1
    //@POST("/v1/registerCustomer") // preprod
    @POST("/Prod/registerCustomer") // test
    // 2
    fun createQuickStreamAccount(@Body requestBody: CreateQuickStreamAccountRequestBody):
            Call<String>
    // 3
    companion object {
        // 4
        val instance: CreateQuickStreamAccountService by lazy {
            // 5
            val client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("Unicard_API", "1@3$"))
            val retrofit = Retrofit.Builder()
                //.baseUrl("https://tafenswpayment.identityone-api.com.au") // preprod
                .baseUrl("https://f3fcdp7la2.execute-api.ap-southeast-2.amazonaws.com") // test
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // 6
            retrofit.create<CreateQuickStreamAccountService>(CreateQuickStreamAccountService::class.java)
        }
    }
}

class CreateQuickStreamAccountRequestBody(unicardID: String, customerName: String, emailAddress: String) {
    var UnicardID = unicardID
    var CustomerName = customerName
    var EmailAddress = emailAddress
}