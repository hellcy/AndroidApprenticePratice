package com.yuan.tafewallet.topup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.MainActivity

import com.yuan.tafewallet.adapters.TopupTransactionCompleteTableViewAdapter
import com.yuan.tafewallet.models.*
import com.yuan.tafewallet.service.GetPaperCutAccountBalanceService
import com.yuan.tafewallet.service.GetPaperCutAccountsRequestBody
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_topup_complete.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TopupCompleteFragment : Fragment() {
    lateinit var account: PaperCutAccount
    var amount: Int = 0
    private lateinit var westpacTransaction: WestpacTransaction
    private lateinit var westpacAccount: WestpacAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        account = arguments?.getParcelable("Account")!!
        amount = arguments?.getInt("Amount")!!
        westpacTransaction = arguments?.getParcelable("WestpacTransaction")!!
        westpacAccount = arguments?.getParcelable("WestpacAccount")!!
}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.title = "Transaction Complete"
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            activity.nav_view.isVisible = false
        }

        val view = inflater.inflate(com.yuan.tafewallet.R.layout.fragment_topup_complete, container, false)
        var updatedBalance = account.Balance
        if (westpacTransaction.status == "Approved") {
            updatedBalance += amount
        }
        view.AccountNameLabel.text = account.AccountName
        view.AccountBalanceLabel.text = (activity as MainActivity).convertDollarSign(updatedBalance)
        retrievePaperCutAccountBalance()
        view.DoneButton.setOnClickListener { v ->
            doneButtonPressed() }


        view.topupTransactionCompleteTable.adapter = TopupTransactionCompleteTableViewAdapter(westpacTransaction)
        view.topupTransactionCompleteTable.layoutManager = LinearLayoutManager(activity)

        return view
    }

    private fun doneButtonPressed() {
        val fragment = TopupFragment.newInstance()
        (activity as MainActivity).clearBackStack()
        (activity as MainActivity).setFragment(fragment)
    }

    private fun retrievePaperCutAccountBalance() {
        val paperCutAccountManager = PaperCutAccountManager(context!!)
        val getPaperCutAccountBalanceService = GetPaperCutAccountBalanceService.instance
        val requestBody = GetPaperCutAccountsRequestBody(account.AccountName!!)
        val request = getPaperCutAccountBalanceService.getPaperCutAccounts(requestBody)

        request.enqueue(object : Callback<ArrayList<PaperCutAccount>> {
            override fun onFailure(call: Call<ArrayList<PaperCutAccount>>, t: Throwable) {
                Log.i(TopupSelectCardFragment.TAG, "Call to ${call.request()?.url()} " + "failed with $t")
            }

            override fun onResponse(
                call: Call<ArrayList<PaperCutAccount>>,
                response: Response<ArrayList<PaperCutAccount>>
            ) {
                Log.i(TopupSelectCardFragment.TAG, "Got response with status code " + "${response.code()} and message " + "$response?.message()")
                if (response.isSuccessful) {
                    paperCutAccountManager.savePaperCutAccount(response.body()!!)
                    paperCutAccountManager.savePrimaryAccount(response.body()!!)
                    Log.i(TopupSelectCardFragment.TAG, "get papercut account balance response body " + paperCutAccountManager.readPaperCutAccounts())
                } else {
                    (activity as MainActivity).showAlert()
                }
            }
        })
    }

    fun onBackPressed(): Boolean {
        val fragment = TopupFragment.newInstance()
        (activity as MainActivity).clearBackStack()
        (activity as MainActivity).setFragment(fragment)
        return true
    }

    companion object {
        val TAG = TopupCompleteFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: PaperCutAccount, amount: Int, westpacTransaction: WestpacTransaction, westpacAccount: WestpacAccount): TopupCompleteFragment {
            val fragment = TopupCompleteFragment()
            val args = Bundle()
            args.putParcelable("Account", account)
            args.putInt("Amount", amount)
            args.putParcelable("WestpacTransaction", westpacTransaction)
            args.putParcelable("WestpacAccount", westpacAccount)
            fragment.arguments = args
            return fragment
        }
    }
}
