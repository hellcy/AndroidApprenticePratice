package com.yuan.tafewallet.service

import com.yuan.tafewallet.models.PaperCutAccount
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface GetPaperCutAccountBalanceService {
    // 1
    //@POST("/v1/getPaperCutAccount") // preprod
    @POST("/Prod/getPaperCutAccount") // test
    // 2
    fun getPaperCutAccounts(@Body requestBody: GetPaperCutAccountsRequestBody):
            Call<ArrayList<PaperCutAccount>>
    // 3
    companion object {
        // 4
        val instance: GetPaperCutAccountBalanceService by lazy {
            // 5
            val client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("Unicard_API", "1@3$"))
            val retrofit = Retrofit.Builder()
                //.baseUrl("https://tafenswpayment.identityone-api.com.au") // preprod
                .baseUrl("https://f3fcdp7la2.execute-api.ap-southeast-2.amazonaws.com") // test
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // 6
            retrofit.create<GetPaperCutAccountBalanceService>(GetPaperCutAccountBalanceService::class.java)
        }
    }
}
