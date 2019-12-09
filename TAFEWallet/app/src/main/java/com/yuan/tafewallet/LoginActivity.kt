package com.yuan.tafewallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    internal lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        loginButton = findViewById(R.id.LoginButton)

        loginButton.setOnClickListener {v -> login()}
    }

    fun login() {
        val homeIntent = Intent(this, MainActivity::class.java)

        startActivity(homeIntent)
    }
}
