package com.yuan.tafewallet.service

import retrofit2.Call
import com.yuan.tafewallet.models.PaperCutAccount
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GetPaperCutAccountsService {
    // 1
    @POST("/TAFEOnsiteAPIForMobileAndWebPortal/api/TAFEOnsiteAPI/RetrieveAllPapercutAccountsByPapercutUsername")
    // 2
    fun getPaperCutAccounts(@Body requestBody: GetPaperCutAccountsRequestBody):
            Call<ArrayList<PaperCutAccount>>
    // 3
    companion object {
        // 4
        val instance: GetPaperCutAccountsService by lazy {
            // 5
            val client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("Unicard_API", "1@3$"))
            val retrofit = Retrofit.Builder()
                .baseUrl("https://idmobile-dev.com.au")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // 6
            retrofit.create<GetPaperCutAccountsService>(GetPaperCutAccountsService::class.java)
        }
    }
}

class GetPaperCutAccountsRequestBody(username: String) {
    var username = username
}