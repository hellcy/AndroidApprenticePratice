package com.yuan.tafewallet

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.yuan.tafewallet.models.PaperCutAccount
import com.yuan.tafewallet.models.PaperCutAccountManager
import com.yuan.tafewallet.service.GetPaperCutAccountsRequestBody
import com.yuan.tafewallet.service.GetPaperCutAccountsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    val TAG = javaClass.simpleName
    internal lateinit var loginButton: Button
    var paperCutAccounts = ArrayList<PaperCutAccount>()
    val paperCutAccountManager = PaperCutAccountManager(this)
    val requestBody = GetPaperCutAccountsRequestBody("a8000755")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        loginButton = findViewById(R.id.LoginButton)

        loginButton.setOnClickListener {v -> login()}
    }

    fun login() {
        val homeIntent = Intent(this, MainActivity::class.java)
        val paperCutAccount1 = PaperCutAccount("Yuan Cheng", "a8000755", 23.954, "a8000755", "USER", "LastActivityDate", "test@unicard.com.au")

        val getPaperCutAccountsService = GetPaperCutAccountsService.instance
        val request = getPaperCutAccountsService.getPaperCutAccounts(requestBody)

        request.enqueue(object : Callback<ArrayList<PaperCutAccount>> {
            override fun onFailure(call: Call<ArrayList<PaperCutAccount>>, t: Throwable) {
                Log.i(TAG, "Call to ${call?.request()?.url()} " + "failed with ${t.toString()}")
            }

            override fun onResponse(
                call: Call<ArrayList<PaperCutAccount>>,
                response: Response<ArrayList<PaperCutAccount>>
            ) {
                Log.i(TAG, "Got response with status code " + "${response?.code()} and message " + "${response?.message()}")
                val body = response?.body()
                Log.i(TAG, "Response body = $body")
            }
        })

        paperCutAccounts.add(paperCutAccount1)
        paperCutAccountManager.savePrimaryAccount(paperCutAccounts)
        startActivity(homeIntent)
    }
}
