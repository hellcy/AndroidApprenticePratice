package com.yuan.tafewallet.service

import com.yuan.tafewallet.models.PaperCutAccount
import com.yuan.tafewallet.models.Transaction
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface GetTransactionHistoryService {
    // 1
    @POST("/TAFEOnsiteAPIForMobileAndWebPortal/api/TAFEOnsiteAPI/RetrieveAllTransactionsByPapercutAccountNameAndDateRange")
    // 2
    fun getTransactionHistory(@Body requestBody: GetTransactionHistoryRequestBody):
            Call<ArrayList<Transaction>>
    // 3
    companion object {
        // 4
        val instance: GetTransactionHistoryService by lazy {
            // 5
            val client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("Unicard_API", "1@3$"))
            val retrofit = Retrofit.Builder()
                .baseUrl("https://idmobile-dev.com.au")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // 6
            retrofit.create<GetTransactionHistoryService>(GetTransactionHistoryService::class.java)
        }
    }
}

class GetTransactionHistoryRequestBody(accountName: String, fromDate: String, toDate: String) {
    var accountName = accountName
    var fromDate = fromDate
    var toDate = toDate
}