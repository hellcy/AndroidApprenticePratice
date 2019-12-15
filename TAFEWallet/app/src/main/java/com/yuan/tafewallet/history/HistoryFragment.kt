package com.yuan.tafewallet.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.MainActivity
import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.HistorySelectAccountTableViewAdapter
import com.yuan.tafewallet.adapters.TopupSelectAccountTableViewAdapter
import com.yuan.tafewallet.models.PaperCutAccount
import com.yuan.tafewallet.models.PaperCutAccountManager
import com.yuan.tafewallet.models.UnicardAccountManager
import com.yuan.tafewallet.models.WestpacAccountManager
import com.yuan.tafewallet.service.GetPaperCutAccountsRequestBody
import com.yuan.tafewallet.service.GetPaperCutAccountsService
import com.yuan.tafewallet.topup.TopupFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_topup.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryFragment : Fragment(), HistorySelectAccountTableViewAdapter.HistorySelectAccountTableViewClickListener {
    lateinit var unicardAccountManager: UnicardAccountManager
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

        val view = inflater.inflate(R.layout.fragment_history, container, false)
        view.historySelectAccountSwipeRefresh.setOnRefreshListener {
            getPaperCutAccounts(view)
        }

        view.HistoryAccountTable.adapter = HistorySelectAccountTableViewAdapter(paperCutAccountManager.readPaperCutAccounts()!!, this)
        view.HistoryAccountTable.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun listItemClicked(position: Int) {
        val fragment = HistoryTransactionsFragment.newInstance(paperCutAccountManager.readPaperCutAccounts()!![position])
        (activity as MainActivity).gotoFragment(fragment, HistoryTransactionsFragment.TAG)
    }

    private fun getPaperCutAccounts(view: View) {
        val getPaperCutAccountsService = GetPaperCutAccountsService.instance
        val requestBody = GetPaperCutAccountsRequestBody(unicardAccountManager.readUnicardAccount().PaperCutID)
        val request = getPaperCutAccountsService.getPaperCutAccounts(requestBody)

        request.enqueue(object : Callback<ArrayList<PaperCutAccount>> {
            override fun onFailure(call: Call<ArrayList<PaperCutAccount>>, t: Throwable) {
                Log.i(TopupFragment.TAG, "Call to ${call?.request()?.url()} " + "failed with ${t.toString()}")
            }

            override fun onResponse(
                call: Call<ArrayList<PaperCutAccount>>,
                response: Response<ArrayList<PaperCutAccount>>
            ) {
                Log.i(TopupFragment.TAG, "Got response with status code " + "${response.code()} and message " + response.message())
                if (response.isSuccessful) {
                    if (response.body()?.size == 0) (activity as MainActivity).showAlert()
                    else {
                        paperCutAccountManager.savePaperCutAccount(response.body()!!) // save to global objects
                        paperCutAccountManager.savePrimaryAccount(response.body()!!)
                        Log.i(TopupFragment.TAG, "get paperCutAccounts response body " + "${paperCutAccountManager.readPaperCutAccounts()}")
                        view.HistoryAccountTable.adapter = HistorySelectAccountTableViewAdapter(paperCutAccountManager.readPaperCutAccounts()!!, this@HistoryFragment)
                        view.historySelectAccountSwipeRefresh.isRefreshing = false
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