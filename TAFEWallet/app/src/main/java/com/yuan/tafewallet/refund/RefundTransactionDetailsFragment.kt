package com.yuan.tafewallet.refund

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager

import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.RefundTransactionDetailsTableViewAdapter
import com.yuan.tafewallet.models.WestpacTransaction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_refund_transaction_details.view.*

class RefundTransactionDetailsFragment : Fragment() {
    private lateinit var refundedTransaction: WestpacTransaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        refundedTransaction = arguments?.getParcelable("RefundedTransaction")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = "Transaction Details"
            activity.nav_view.isVisible = false
        }

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_refund_transaction_details, container, false)
        view.refundTransactionDetailsTable.adapter = RefundTransactionDetailsTableViewAdapter(refundedTransaction)
        view.refundTransactionDetailsTable.layoutManager = LinearLayoutManager(activity)
        view.BackButton.setOnClickListener {
            backButtonPressed()
        }
        return view
    }

    private fun backButtonPressed() {
        activity?.onBackPressed()
    }

    companion object {
        val TAG = RefundTransactionDetailsFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(refundedTransaction: WestpacTransaction): RefundTransactionDetailsFragment {
            val fragment = RefundTransactionDetailsFragment()
            val args = Bundle()
            args.putParcelable("RefundedTransaction", refundedTransaction)
            fragment.arguments = args
            return fragment
        }
    }
}
