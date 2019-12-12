package com.yuan.tafewallet.history

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.MainActivity

import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.HistoryTransactionsTableViewAdapter
import com.yuan.tafewallet.models.*
import com.yuan.tafewallet.topup.TopupCardDetailsFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_history_transactions.view.*

class HistoryTransactionsFragment : Fragment(), HistoryTransactionsTableViewAdapter.HistoryTransactionsTableViewClickListener {
    lateinit var paperCutAccount: PaperCutAccount
    var transactions = ArrayList<Transaction>()
    val transaction1 = Transaction("ADJUST", "Unicard Top up-123456789", 5.0, "TransactionDate", "Y")
    val transaction2 = Transaction("ADJUST", "Unicard Top up-123456789", 8.0, "TransactionDate", "Y")
    val transaction3 = Transaction("ADJUST", "Unicard Refund-123456789", -3.0, "TransactionDate", "Y")
    val transaction4 = Transaction("ADJUST", "Unicard Top up-123456789", 15.0, "TransactionDate", "Y")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paperCutAccount = arguments?.getParcelable("Account")!!
        transactions.add(transaction1)
        transactions.add(transaction2)
        transactions.add(transaction3)
        transactions.add(transaction4)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.title = paperCutAccount.AccountName
            activity.nav_view.isVisible = false
        }

        val view = inflater.inflate(R.layout.fragment_history_transactions, container, false)
        view.AccountNameLabel.text = paperCutAccount.AccountName
        view.AccountBalanceLabel.text = "$" + "%.2f".format(paperCutAccount.Balance)

        view.historyTransactionsTable.adapter = HistoryTransactionsTableViewAdapter(transactions, this)
        view.historyTransactionsTable.layoutManager = LinearLayoutManager(activity)

        return view
    }

    override fun listItemClicked(position: Int) {
        val fragment = HistoryTransactionDetailsFragment.newInstance(transactions[position])
        (activity as MainActivity).gotoFragment(fragment, HistoryTransactionDetailsFragment.TAG)
    }

    companion object {
        val TAG = HistoryTransactionsFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: PaperCutAccount): HistoryTransactionsFragment {
            val fragment = HistoryTransactionsFragment()
            val args = Bundle()
            args.putParcelable("Account", account)
            fragment.arguments = args
            return fragment
        }
    }

}
