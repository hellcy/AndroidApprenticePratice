package com.yuan.tafewallet.service

import com.yuan.tafewallet.models.Transaction
import com.yuan.tafewallet.models.WestpacTransaction
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface GetRefundTransactionService {
    // 1
    @POST("/v1/getRefundTransactions")
    // 2
    fun getRefundTransactions(@Body requestBody: GetRefundTransactionsRequestBody):
            Call<ArrayList<WestpacTransaction>>
    // 3
    companion object {
        // 4
        val instance: GetRefundTransactionService by lazy {
            // 5
            val client = OkHttpClient.Builder().addInterceptor(BasicAuthInterceptor("Unicard_API", "1@3$"))
            val retrofit = Retrofit.Builder()
                .baseUrl("https://tafenswpayment.identityone-api.com.au")
                .client(client.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // 6
            retrofit.create<GetRefundTransactionService>(GetRefundTransactionService::class.java)
        }
    }
}

class GetRefundTransactionsRequestBody(unicardID: String, Amount: String) {
    var UnicardID = unicardID
    var Amount = Amount
}