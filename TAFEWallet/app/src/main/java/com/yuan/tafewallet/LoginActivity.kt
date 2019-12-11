package com.yuan.tafewallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.yuan.tafewallet.models.PaperCutAccount
import com.yuan.tafewallet.models.PaperCutAccountManager

class LoginActivity : AppCompatActivity() {
    internal lateinit var loginButton: Button
    var paperCutAccounts = ArrayList<PaperCutAccount>()
    val paperCutAccountManager = PaperCutAccountManager(this)

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
        paperCutAccounts.add(paperCutAccount1)
        paperCutAccountManager.savePrimaryAccount(paperCutAccounts)
        startActivity(homeIntent)
    }
}
