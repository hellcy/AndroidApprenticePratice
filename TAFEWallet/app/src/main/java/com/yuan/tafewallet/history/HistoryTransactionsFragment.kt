package com.yuan.tafewallet.history

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.DatePickerFragment
import com.yuan.tafewallet.MainActivity
import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.HistoryTransactionsTableViewAdapter
import com.yuan.tafewallet.models.PaperCutAccount
import com.yuan.tafewallet.models.Transaction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_history_transactions.view.*


class HistoryTransactionsFragment : Fragment(), HistoryTransactionsTableViewAdapter.HistoryTransactionsTableViewClickListener {
    val fromDateCode: Int = 1
    val toDateCode: Int = 2
    var selectedDate = ""
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
        view.FromDate.setOnClickListener {
            // create the datePickerFragment
            val newFragment: AppCompatDialogFragment = DatePickerFragment()
            // set the targetFragment to receive the results, specifying the request code
            newFragment.setTargetFragment(this, fromDateCode)
            // show the datePicker
            newFragment.show(activity!!.supportFragmentManager, "datePicker")
        }

        view.ToDate.setOnClickListener {
            val newFragment: AppCompatDialogFragment = DatePickerFragment()
            newFragment.setTargetFragment(this, toDateCode)
            newFragment.show(activity!!.supportFragmentManager, "datePicker")
        }

        view.SearchButton.setOnClickListener { v ->
            searchButtonPressed()
        }

        view.historyTransactionsTable.adapter = HistoryTransactionsTableViewAdapter(transactions, this)
        view.historyTransactionsTable.layoutManager = LinearLayoutManager(activity)

        return view
    }

    override fun listItemClicked(position: Int) {
        val fragment = HistoryTransactionDetailsFragment.newInstance(transactions[position])
        (activity as MainActivity).gotoFragment(fragment, HistoryTransactionDetailsFragment.TAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == fromDateCode && resultCode == Activity.RESULT_OK) { // get date from string
            selectedDate = data!!.getStringExtra("selectedDate")
            // set the value of the editText
            view?.FromDate?.text = selectedDate
        }

        if (requestCode == toDateCode && resultCode == Activity.RESULT_OK) { // get date from string
            selectedDate = data!!.getStringExtra("selectedDate")
            // set the value of the editText
            view?.ToDate?.text = selectedDate
        }
    }

    private fun searchButtonPressed() {
        //TODO: call api to get transaction history and pass it to table adapter
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
