package com.yuan.tafewallet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.yuan.tafewallet.models.PaperCutAccount
import com.yuan.tafewallet.models.PaperCutAccountManager
import com.yuan.tafewallet.models.UnicardAccount
import com.yuan.tafewallet.models.UnicardAccountManager
import com.yuan.tafewallet.service.*
import com.yuan.tafewallet.topup.TopupNewCardFragment
import kotlinx.android.synthetic.main.fragment_topup_new_card.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    val TAG = javaClass.simpleName
    internal lateinit var loginButton: Button
    lateinit var webView: CustomWebView
    var paperCutID: String? = "a8000761"
    val emailAddress = "testAndroid@unicard.com.au"
    val paperCutAccountManager = PaperCutAccountManager(this)
    val unicardAccountManager = UnicardAccountManager(this)
    var savedPaperCutID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        loginButton = findViewById(R.id.LoginButton)
        webView = findViewById(R.id.webView)
        loginButton.setOnClickListener {v -> loginButtonPressed()}

        savedPaperCutID = unicardAccountManager.readPaperCutID()

        if (savedPaperCutID != "" && savedPaperCutID != null) {
            getPaperCutAccounts()
        }

        webView.loadUrl("https://tafensw.identityone-api.com.au/sso_saml/mobile-preprod")
        webView.settings.javaScriptEnabled = true
        webView.settings.saveFormData = false
        webView.addJavascriptInterface(WebAppInterface(this), "userLogin")
        webView.webViewClient = WebViewClient()
    }


    class WebAppInterface(val activity: LoginActivity) {
        @JavascriptInterface
        fun postMessage(paperCutID: String) {
            activity.paperCutID = paperCutID
            activity.performWebViewRequest()
        }
    }

    fun performWebViewRequest() {
        // first time log in
        getPaperCutAccounts()
    }

    fun loginButtonPressed() {
        unicardAccountManager.savePaperCutID("a8000761")
        getPaperCutAccounts()
    }

    fun getPaperCutAccounts() {
        val getPaperCutAccountsService = GetPaperCutAccountsService.instance
        val requestBody = GetPaperCutAccountsRequestBody(paperCutID!!)
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
                if (response.isSuccessful) {
                    if (response?.body()?.size == 0) showAlert()
                    else {
                        paperCutAccountManager.savePaperCutAccount(response?.body()!!) // save to global objects
                        paperCutAccountManager.savePrimaryAccount(response?.body()!!)
                        Log.i(TAG, "get paperCutAccounts response body " + "${paperCutAccountManager.readPaperCutAccounts()}")

                        getUnicardAccountAndLogin()
                    }
                } else {
                    showAlert()
                }
            }
        })
    }

    fun getUnicardAccountAndLogin() {
        val generateUnicardIDService = GenerateUnicardIDService.instance
        val requestBody = GenerateUnicardIDRequestBody("",paperCutID!!, "",emailAddress, "")
        val request = generateUnicardIDService.generateUnicardID(requestBody)

        request.enqueue(object : Callback<ArrayList<UnicardAccount>> {
            override fun onFailure(call: Call<ArrayList<UnicardAccount>>, t: Throwable) {
                Log.i(TAG, "Call to ${call?.request()?.url()} " + "failed with ${t.toString()}")
            }

            override fun onResponse(
                call: Call<ArrayList<UnicardAccount>>,
                response: Response<ArrayList<UnicardAccount>>
            ) {
                Log.i(TAG, "Got response with status code " + "${response?.code()} and message " + "${response?.message()}")
                if (response.isSuccessful) {
                    if (response?.body()?.size == 0) showAlert()
                    else {
                        unicardAccountManager.saveUnicardAccount(response?.body()!![0]) // save to global objects
                        unicardAccountManager.savePaperCutID(paperCutAccountManager.readPrimaryAccount().UserName!!)
                        Log.i(TAG, "get Unicard account response body " + "${unicardAccountManager.readUnicardAccount()}")

                        val intent = Intent(applicationContext,MainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    showAlert()
                }
            }
        })
    }

    fun showAlert() {
        val alert = AlertDialog.Builder(this@LoginActivity)
        alert.setTitle("Whoops...")
            .setMessage("Action failed, please try again later. Make sure you have a valid PaperCut Account")
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}
