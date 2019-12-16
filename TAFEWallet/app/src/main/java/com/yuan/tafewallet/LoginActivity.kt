package com.yuan.tafewallet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.yuan.tafewallet.models.PaperCutAccount
import com.yuan.tafewallet.models.PaperCutAccountManager
import com.yuan.tafewallet.models.UnicardAccount
import com.yuan.tafewallet.models.UnicardAccountManager
import com.yuan.tafewallet.service.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    val TAG = javaClass.simpleName
    internal lateinit var loginButton: Button
    val paperCutID = "a8000761"
    val emailAddress = "yuanAndroid@unicard.com.au"
    var quickStreamID = ""
    val paperCutAccountManager = PaperCutAccountManager(this)
    val unicardAccountManager = UnicardAccountManager(this)
    var savedPaperCutID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        loginButton = findViewById(R.id.LoginButton)

        loginButton.setOnClickListener {v -> loginButtonPressed()}
        savedPaperCutID = unicardAccountManager.readPaperCutID()

        unicardAccountManager.savePaperCutID("a8000761")
        // TODO: Get information from TAFE SSO

        // first time log in
        if (savedPaperCutID != null) {
            // TODO: add BioID Check here
            getPaperCutAccounts()
            getUnicardAccountAndLogin()
        } else {
            // TODO: - load SSO page and get PaperCut ID and Email Address
            // Then use the information to create Unicard ID and QuickStream ID
            // Now assume we already have PaperCut ID and Email

            generateUnicardID()
        }
    }

    fun loginButtonPressed() {
        getPaperCutAccounts()
        getUnicardAccountAndLogin()
    }

    fun getPaperCutAccounts() {
        val getPaperCutAccountsService = GetPaperCutAccountsService.instance
        val requestBody = GetPaperCutAccountsRequestBody(paperCutID)
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
                        if (savedPaperCutID == null) {
                            createQuickStreamAccount()
                        }
                    }
                } else {
                    showAlert()
                }
            }
        })
    }

    fun createQuickStreamAccount() {
        val email = if (paperCutAccountManager.readPrimaryAccount().Email == null) "" else paperCutAccountManager.readPrimaryAccount().Email!!
        val createQuickStreamAccountService = CreateQuickStreamAccountService.instance
        val requestBody = CreateQuickStreamAccountRequestBody(unicardAccountManager.readUnicardAccount().UnicardID,
            paperCutAccountManager.readPrimaryAccount().FullName!!,
            email)

        val request = createQuickStreamAccountService.createQuickStreamAccount(requestBody)

        request.enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i(TAG, "Call to ${call?.request()?.url()} " + "failed with ${t.toString()}")
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.i(TAG, "Got response with status code " + "${response?.code()} and message " + "${response?.message()}")
                if (response.isSuccessful) {
                    quickStreamID = response?.body()!!
                    Log.i(TAG, "get paperCutAccounts response body " + quickStreamID)

                    updateUnicardAccount()
                } else {
                    showAlert()
                }
            }
        })
    }

    fun updateUnicardAccount() {
        val updateUnicardAccountService = UpdateUnicardAccountService.instance
        val requestBody = UpdateUnicardAccountRequestBody(
            unicardAccountManager.readUnicardAccount().UnicardID,
            unicardAccountManager.readUnicardAccount().PaperCutID,
            quickStreamID,
            unicardAccountManager.readUnicardAccount().Email,
            "")
        val request = updateUnicardAccountService.updateUnicardAccount(requestBody)

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
                        Log.i(TAG, "update Unicard Account response body " + "${unicardAccountManager.readUnicardAccount()}")
                        unicardAccountManager.savePaperCutID(unicardAccountManager.readUnicardAccount().PaperCutID)
                        val intent = Intent(applicationContext,MainActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    showAlert()
                }
            }
        })
    }

    fun generateUnicardID() {
        val generateUnicardIDService = GenerateUnicardIDService.instance
        val requestBody = GenerateUnicardIDRequestBody("",paperCutID, "",emailAddress, "")
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
                        Log.i(TAG, "generate Unicard ID response body " + "${unicardAccountManager.readUnicardAccount()}")
                        getPaperCutAccounts()
                    }
                } else {
                    showAlert()
                }
            }
        })
    }

    fun getUnicardAccountAndLogin() {
        val generateUnicardIDService = GenerateUnicardIDService.instance
        val requestBody = GenerateUnicardIDRequestBody("",paperCutID, "",emailAddress, "")
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
            .setMessage("Action failed, please try again later")
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }
}
