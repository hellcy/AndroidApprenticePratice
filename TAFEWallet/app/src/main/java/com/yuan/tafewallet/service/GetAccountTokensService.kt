package com.yuan.tafewallet.service

import com.yuan.tafewallet.models.UnicardAccount
import com.yuan.tafewallet.models.WestpacAccount
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface GetAccountTokensService {
    // 1
    @POST("/v1/getAccountTokensByCustomerId")
    // 2
    fun getAccountTokens(@Body requestBody: GetAccountTokensRequestBody):
            Call<ArrayList<WestpacAccount>>
    // 3
    companion object {
        // 4
        val instance: GetAccountTokensService by lazy {
            // 5
            val client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("Unicard_API", "1@3$"))
            val retrofit = Retrofit.Builder()
                .baseUrl("https://tafenswpayment.identityone-api.com.au")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // 6
            retrofit.create<GetAccountTokensService>(GetAccountTokensService::class.java)
        }
    }
}

class GetAccountTokensRequestBody(quickStreamID: String) {
    var QuickStreamID = quickStreamID
}