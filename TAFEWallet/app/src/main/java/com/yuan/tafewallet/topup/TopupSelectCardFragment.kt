package com.yuan.tafewallet.topup

import android.content.Context
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

import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.TopupSelectCardTableViewAdapter
import com.yuan.tafewallet.models.*
import com.yuan.tafewallet.service.GetAccountTokensRequestBody
import com.yuan.tafewallet.service.GetAccountTokensService
import com.yuan.tafewallet.service.GetPaperCutAccountsRequestBody
import com.yuan.tafewallet.service.GetPaperCutAccountsService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_topup_select_card.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TopupSelectCardFragment : Fragment(), TopupSelectCardTableViewAdapter.TopupSelectCardTableViewClickListener {
    lateinit var account: PaperCutAccount
    var amount: Int = 0
    lateinit var savedAccounts: ArrayList<WestpacAccount>
    lateinit var westpacAccountManager: WestpacAccountManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        westpacAccountManager = WestpacAccountManager(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = arguments?.getParcelable("Account")!!
        amount = arguments?.getInt("Amount")!!
        getAccountTokens()
        savedAccounts = westpacAccountManager.readWestpacAccounts()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.title = "Select Credit/Debit Card"
            activity.nav_view.isVisible = false
        }

        val view = inflater.inflate(R.layout.fragment_topup_select_card, container, false)
        view.AccountNameLabel.text = account.AccountName
        view.AccountBalanceLabel.text = "$" + "%.2f".format(account.Balance)
        view.UseNewCardButton.setOnClickListener { v ->
            useNewCardButtonPressed()
        }
        view.topupSelectCardTable.adapter = TopupSelectCardTableViewAdapter(savedAccounts, this)
        view.topupSelectCardTable.layoutManager = LinearLayoutManager(activity)

        return view
    }

    // go to saved cards
    override fun listItemClicked(position: Int) {
        val fragment = TopupCardDetailsFragment.newInstance(account, savedAccounts[position], amount)
        (activity as MainActivity).gotoFragment(fragment, TopupCardDetailsFragment.TAG)
    }

    // go to new card
    private fun useNewCardButtonPressed() {
        val fragment = TopupNewCardFragment.newInstance(account, amount)
        (activity as MainActivity).gotoFragment(fragment, TopupNewCardFragment.TAG)
    }

    fun getAccountTokens() {
        val unicardAccountManager = UnicardAccountManager(context!!)

        val getAccountTokensService = GetAccountTokensService.instance
        val requestBody = GetAccountTokensRequestBody(unicardAccountManager.readUnicardAccount().QuickStreamID!!)
        val request = getAccountTokensService.getAccountTokens(requestBody)

        request.enqueue(object : Callback<ArrayList<WestpacAccount>> {
            override fun onFailure(call: Call<ArrayList<WestpacAccount>>, t: Throwable) {
                Log.i(TAG, "Call to ${call?.request()?.url()} " + "failed with ${t.toString()}")
            }

            override fun onResponse(
                call: Call<ArrayList<WestpacAccount>>,
                response: Response<ArrayList<WestpacAccount>>
            ) {
                Log.i(TAG, "Got response with status code " + "${response?.code()} and message " + "${response?.message()}")
                if (response.isSuccessful) {
                    westpacAccountManager.saveWestpacAccount(response?.body()!!) // save to global objects
                    Log.i(TAG, "get westpac account tokens response body " + "${westpacAccountManager.readWestpacAccounts()}")
                } else {
                    (activity as MainActivity).showAlert()
                }
            }
        })
    }

    companion object {
        val TAG = TopupSelectCardFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: PaperCutAccount, amount: Int): TopupSelectCardFragment {
            val fragment = TopupSelectCardFragment()
            val args = Bundle()
            args.putParcelable("Account", account)
            args.putInt("Amount", amount)
            fragment.arguments = args
            return fragment
        }
    }
}
