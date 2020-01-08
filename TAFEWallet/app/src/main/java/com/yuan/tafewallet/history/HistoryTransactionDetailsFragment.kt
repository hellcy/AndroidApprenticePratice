package com.yuan.tafewallet.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager

import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.HistoryTransactionDetailsTableViewAdapter
import com.yuan.tafewallet.models.Transaction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_history_transaction_details.view.*

class HistoryTransactionDetailsFragment : Fragment() {
    lateinit var transaction: Transaction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        transaction = arguments?.getParcelable("Transaction")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = "Details"
            activity.nav_view.isVisible = false
        }

        val view = inflater.inflate(R.layout.fragment_history_transaction_details, container, false)
        view.historyTransactionsDetailsTable.adapter = HistoryTransactionDetailsTableViewAdapter(transaction)
        view.historyTransactionsDetailsTable.layoutManager = LinearLayoutManager(activity)
        view.BackButton.setOnClickListener() {
            backButtonPressed()
        }
        return view
    }

    private fun backButtonPressed() {
        activity?.onBackPressed()
    }

    companion object {
        val TAG = HistoryTransactionDetailsFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(transaction: Transaction): HistoryTransactionDetailsFragment {
            val fragment = HistoryTransactionDetailsFragment()
            val args = Bundle()
            args.putParcelable("Transaction", transaction)
            fragment.arguments = args
            return fragment
        }
    }
}
