package com.yuan.tafewallet.topup

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.CustomProgressBar
import com.yuan.tafewallet.MainActivity
import com.yuan.tafewallet.PopupMeaningFragment
import com.yuan.tafewallet.TAFEWalletApplication
import com.yuan.tafewallet.adapters.TopupCardDetailsTableViewAdapter
import com.yuan.tafewallet.dagger.AppComponent
import com.yuan.tafewallet.models.*
import com.yuan.tafewallet.service.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_topup_confirm.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class TopupConfirmFragment : Fragment() {
    @Inject
    lateinit var mContext: Context

    lateinit var account: PaperCutAccount
    lateinit var westpacAccount: WestpacAccount
    var westpacTransaction = WestpacTransaction()
    var registeredWestpacAccount = WestpacAccount()
    var secretToken: String? = null
    var amount: Int = 0
    var saveCard: Boolean = false
    val progressBar = CustomProgressBar()
    lateinit var unicardAccountManager: UnicardAccountManager
    lateinit var paperCutAccountManager: PaperCutAccountManager
    var quickStreamID = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        unicardAccountManager = UnicardAccountManager(context)
        paperCutAccountManager = PaperCutAccountManager(context)
        (activity?.application as TAFEWalletApplication).mainComponent.injectTopupConfirmFragment(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = arguments?.getParcelable("Account")!!
        westpacAccount = arguments?.getParcelable("WestpacAccount")!!
        amount = arguments?.getInt("Amount")!!
        secretToken = arguments?.getString("SecretToken")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = "Confirm Top Up"
            activity.nav_view.isVisible = false
        }

        val view = inflater.inflate(com.yuan.tafewallet.R.layout.fragment_topup_confirm, container, false)
        view.AccountNameLabel.text = account.AccountName
        view.AccountBalanceLabel.text = (activity as MainActivity).convertDollarSign(account.Balance)
        view.Amount.text = (activity as MainActivity).convertDollarSign(amount.toDouble())

        if (secretToken == null) {
            view.meaningLabel.isVisible = false
            view.saveCardBox.isVisible = false
        }

        val text = "What does it mean?"
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, text.length, 0)
        view.meaningLabel.setText(content)
        view.meaningLabel.setOnClickListener {
            val newFragment = PopupMeaningFragment()
            val dialogFragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            val prev = activity!!.supportFragmentManager.findFragmentByTag("dialog")
            if (prev != null) {
                dialogFragmentTransaction.remove(prev)
            }
            dialogFragmentTransaction.addToBackStack(null)
            newFragment.show(dialogFragmentTransaction!!, "dialog")
        }

        view.ConfirmButton.setOnClickListener { v ->
            confirmButtonPressed() }

        view.topupConfirmCardDetailsTable.adapter = TopupCardDetailsTableViewAdapter(westpacAccount)
        view.topupConfirmCardDetailsTable.layoutManager = LinearLayoutManager(activity)
        return view
    }

    private fun confirmButtonPressed() {
        progressBar.show(context!!,"Transaction in progress... \nPlease do not close or exit this application")
        if (unicardAccountManager.readUnicardAccount().QuickStreamID == null) {
            createQuickStreamAccount()
        } else {
            if (secretToken != null) {
                // use new card
                performTopupBySingleUseToken()
            } else {
                // use saved card
                performTopupByAccountToken()
            }
        }
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
                    (activity as MainActivity).showAlert()
                    progressBar.dialog.dismiss()
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
                    if (response?.body()?.size == 0) {
                        (activity as MainActivity).showAlert()
                        progressBar.dialog.dismiss()
                    }
                    else {
                        unicardAccountManager.saveUnicardAccount(response?.body()!![0]) // save to global objects
                        Log.i(TAG, "update Unicard Account response body " + "${unicardAccountManager.readUnicardAccount()}")

                        if (secretToken != null) {
                            // use new card
                            performTopupBySingleUseToken()
                        } else {
                            // use saved card
                            performTopupByAccountToken()
                        }
                    }
                } else {
                    (activity as MainActivity).showAlert()
                    progressBar.dialog.dismiss()
                }
            }
        })
    }

    fun performTopupByAccountToken() {
        val unicardAccountManager = UnicardAccountManager(context!!)

        val processTopupTransactionService = ProcessTopupTransactionService.instance
        val requestBody = TopupByAccountTokenRequestBody(westpacAccount.accountToken, amount.toString(), unicardAccountManager.readUnicardAccount().UnicardID, unicardAccountManager.readUnicardAccount().PaperCutID)
        val request = processTopupTransactionService.topupByAccountToken(requestBody)

        request.enqueue(object : Callback<WestpacTransaction> {
            override fun onFailure(call: Call<WestpacTransaction>, t: Throwable) {
                Log.i(TopupSelectCardFragment.TAG, "Call to ${call?.request()?.url()} " + "failed with ${t.toString()}")
            }

            override fun onResponse(
                call: Call<WestpacTransaction>,
                response: Response<WestpacTransaction>
            ) {
                Log.i(TopupSelectCardFragment.TAG, "Got response with status code " + "${response?.code()} and message " + "${response?.message()}")
                if (response.isSuccessful) {
                    westpacTransaction = response?.body()!!
                    Log.i(TopupSelectCardFragment.TAG, "topup by account token Westpac transaction response body " + westpacTransaction)
                    progressBar.dialog.dismiss()
                    if (this@TopupConfirmFragment != null && this@TopupConfirmFragment.isVisible) {
                        val fragment = TopupCompleteFragment.newInstance(account, amount, westpacTransaction, westpacAccount)
                        (activity as MainActivity).gotoFragment(fragment, TopupCompleteFragment.TAG)
                    } else {
                        Toast.makeText(mContext, "Top up succeeded", Toast.LENGTH_LONG).show()
                    }
                } else {
                    (activity as MainActivity).showAlert()
                    progressBar.dialog.dismiss()
                }

            }
        })
    }

    fun performTopupBySingleUseToken() {
        val unicardAccountManager = UnicardAccountManager(context!!)

        val processTopupTransactionService = ProcessTopupTransactionService.instance
        val requestBody = TopupBySingleUseTokenRequestBody(secretToken!!, amount.toString(), unicardAccountManager.readUnicardAccount().UnicardID, "", unicardAccountManager.readUnicardAccount().PaperCutID)
        val request = processTopupTransactionService.topupBySingleUseToken(requestBody)

        request.enqueue(object : Callback<WestpacTransaction> {
            override fun onFailure(call: Call<WestpacTransaction>, t: Throwable) {
                Log.i(TopupSelectCardFragment.TAG, "Call to ${call?.request()?.url()} " + "failed with ${t.toString()}")
            }

            override fun onResponse(
                call: Call<WestpacTransaction>,
                response: Response<WestpacTransaction>
            ) {
                Log.i(TopupSelectCardFragment.TAG, "Got response with status code " + "${response?.code()} and message " + "${response?.message()}")
                if (response.isSuccessful) {
                    westpacTransaction = response?.body()!!
                    Log.i(TopupSelectCardFragment.TAG, "topup by single use token Westpac transaction response body " + westpacTransaction)
                    performRegisterAccount(unicardAccountManager.readUnicardAccount().QuickStreamID!!, westpacTransaction.receiptNumber)
                } else {
                    (activity as MainActivity).showAlert()
                    progressBar.dialog.dismiss()
                }
            }
        })
    }

    fun performRegisterAccount(quickStreamID: String, receiptNumber: String) {
        saveCard = view?.saveCardBox?.isChecked!!
        if (saveCard) {
            val registerAccountService = RegisterAccountService.instance
            val requestBody = RegisterAccountRequestBody(quickStreamID, receiptNumber)
            val request = registerAccountService.registerAccount(requestBody)

            request.enqueue(object : Callback<WestpacAccount> {
                override fun onFailure(call: Call<WestpacAccount>, t: Throwable) {
                    Log.i(TopupSelectCardFragment.TAG, "Call to ${call?.request()?.url()} " + "failed with ${t.toString()}")
                }

                override fun onResponse(
                    call: Call<WestpacAccount>,
                    response: Response<WestpacAccount>
                ) {
                    Log.i(TopupSelectCardFragment.TAG, "Got response with status code " + "${response?.code()} and message " + "${response?.message()}")
                    if (response.isSuccessful) {
                        registeredWestpacAccount = response?.body()!!
                        Log.i(TopupSelectCardFragment.TAG, "register Westpac account response body " + registeredWestpacAccount)
                        progressBar.dialog.dismiss()
                        val fragment = TopupCompleteFragment.newInstance(account, amount, westpacTransaction, westpacAccount)
                        (activity as MainActivity).gotoFragment(fragment, TopupCompleteFragment.TAG)
                    } else {
                        (activity as MainActivity).showAlert()
                        progressBar.dialog.dismiss()
                    }
                }
            })
        } else {
            progressBar.dialog.dismiss()
            val fragment = TopupCompleteFragment.newInstance(account, amount, westpacTransaction, westpacAccount)
            (activity as MainActivity).gotoFragment(fragment, TopupCompleteFragment.TAG)
        }
    }

    companion object {
        val TAG = TopupConfirmFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: PaperCutAccount, westpacAccount: WestpacAccount, amount: Int, secretToken: String?): TopupConfirmFragment {
            val fragment = TopupConfirmFragment()
            val args = Bundle()
            args.putParcelable("Account", account)
            args.putParcelable("WestpacAccount", westpacAccount)
            args.putInt("Amount", amount)
            args.putString("SecretToken", secretToken)
            fragment.arguments = args
            return fragment
        }
    }
}
