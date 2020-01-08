package com.yuan.tafewallet.topup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.MainActivity
import com.yuan.tafewallet.adapters.TopupSelectAccountTableViewAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_topup.view.*
import android.util.Log
import com.yuan.tafewallet.models.*
import com.yuan.tafewallet.service.GetPaperCutAccountsRequestBody
import com.yuan.tafewallet.service.GetPaperCutAccountsService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TopupFragment : Fragment(), TopupSelectAccountTableViewAdapter.TopupSelectAccountTableViewClickListener {
    private lateinit var unicardAccountManager: UnicardAccountManager
    lateinit var paperCutAccountManager: PaperCutAccountManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        unicardAccountManager = UnicardAccountManager(context)
        paperCutAccountManager = PaperCutAccountManager(context)
    }

    // initialize all elements here
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.hide()
            activity.nav_view.isVisible = true
        }

        val view = inflater.inflate(com.yuan.tafewallet.R.layout.fragment_topup, container, false)

        view.topupSelectAccountSwipeRefresh.setOnRefreshListener {
            getPaperCutAccounts(view)
        }

        view.TopUpAccountTable.adapter = TopupSelectAccountTableViewAdapter(paperCutAccountManager.readPaperCutAccounts()!!, this)
        view.TopUpAccountTable.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun listItemClicked(account: PaperCutAccount) {
        val fragment = TopupSelectAmountFragment.newInstance(account)
        (activity as MainActivity).gotoFragment(fragment, TopupSelectAmountFragment.TAG)
    }

    private fun getPaperCutAccounts(view: View) {
        val getPaperCutAccountsService = GetPaperCutAccountsService.instance
        val requestBody = GetPaperCutAccountsRequestBody(unicardAccountManager.readUnicardAccount().PaperCutID)
        val request = getPaperCutAccountsService.getPaperCutAccounts(requestBody)

        request.enqueue(object : Callback<ArrayList<PaperCutAccount>> {
            override fun onFailure(call: Call<ArrayList<PaperCutAccount>>, t: Throwable) {
                Log.i(TAG, "Call to ${call.request()?.url()} " + "failed with $t")
            }

            override fun onResponse(
                call: Call<ArrayList<PaperCutAccount>>,
                response: Response<ArrayList<PaperCutAccount>>
            ) {
                Log.i(TAG, "Got response with status code " + "${response.code()} and message " + response.message())
                if (response.isSuccessful) {
                    if (response.body()?.size == 0) (activity as MainActivity).showAlert()
                    else {
                        paperCutAccountManager.savePaperCutAccount(response.body()!!) // save to global objects
                        paperCutAccountManager.savePrimaryAccount(response.body()!!)
                        Log.i(TAG, "get paperCutAccounts response body " + "${paperCutAccountManager.readPaperCutAccounts()}")
                        view.TopUpAccountTable.adapter = TopupSelectAccountTableViewAdapter(paperCutAccountManager.readPaperCutAccounts()!!, this@TopupFragment)
                        view.topupSelectAccountSwipeRefresh.isRefreshing = false
                    }
                } else {
                    (activity as MainActivity).showAlert()
                }
            }
        })
    }

    companion object {
        val TAG = TopupFragment::class.java.simpleName
        @JvmStatic
        fun newInstance() = TopupFragment()
    }
}