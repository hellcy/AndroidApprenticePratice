package com.yuan.tafewallet.history

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
import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.HistorySelectAccountTableViewAdapter
import com.yuan.tafewallet.models.PaperCutAccount
import com.yuan.tafewallet.models.PaperCutAccountManager
import com.yuan.tafewallet.models.WestpacAccountManager
import com.yuan.tafewallet.topup.TopupFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_history.view.*

class HistoryFragment : Fragment(), HistorySelectAccountTableViewAdapter.HistorySelectAccountTableViewClickListener {
    // sample data
    val paperCutAccount1 = PaperCutAccount("Yuan Cheng", "a8000755", 23.954, "a8000755", "USER", "LastActivityDate", "test@unicard.com.au")
    val paperCutAccount2 = PaperCutAccount("Yuan Cheng", "a8000755", 200.0, "Software Development", "SHARED", "LastActivityDate", "test@unicard.com.au")
    lateinit var savedPaperCutAccounts: ArrayList<PaperCutAccount>
    lateinit var paperCutAccountManager: PaperCutAccountManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        paperCutAccountManager = PaperCutAccountManager(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var paperCutAccounts = ArrayList<PaperCutAccount>()
        paperCutAccounts.add(paperCutAccount1)
        paperCutAccounts.add(paperCutAccount2)
        paperCutAccountManager.savePaperCutAccount(paperCutAccounts)
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
        savedPaperCutAccounts = paperCutAccountManager.readPaperCutAccounts()!!

        val view = inflater.inflate(R.layout.fragment_history, container, false)
        view.HistoryAccountTable.adapter = HistorySelectAccountTableViewAdapter(savedPaperCutAccounts, this)
        view.HistoryAccountTable.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun listItemClicked(position: Int) {
        val fragment = HistoryTransactionsFragment.newInstance(savedPaperCutAccounts[position])
        (activity as MainActivity).gotoFragment(fragment, HistoryTransactionsFragment.TAG)
    }

    companion object {
        val TAG = TopupFragment::class.java.simpleName
        @JvmStatic
        fun newInstance() = TopupFragment()
    }
}