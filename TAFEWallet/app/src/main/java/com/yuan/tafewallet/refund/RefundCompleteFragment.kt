package com.yuan.tafewallet.refund

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.WestpacTransaction

class RefundCompleteFragment : Fragment() {
    lateinit var transactions: ArrayList<WestpacTransaction>
    var refundedAmount: Double = 0.0
    var refundAmount: Double = 0.0
    var updatedBalance: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        transactions = arguments?.getParcelableArrayList<WestpacTransaction>("Transactions")!!
        refundedAmount = arguments?.getDouble("RefundedAmount")!!
        refundAmount = arguments?.getDouble("RefundAmount")!!
        updatedBalance = arguments?.getDouble("UpdatedBalance")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_refund_complete, container, false)
    }

    companion object {
        val TAG = RefundCompleteFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(transactions: ArrayList<WestpacTransaction>, refundedAmount: Double, refundAmount: Double, updatedBalance: Double): RefundCompleteFragment {
            val fragment = RefundCompleteFragment()
            val args = Bundle()
            args.putParcelableArrayList("Transactions", transactions)
            args.putDouble("RefundedAmount", refundedAmount)
            args.putDouble("RefundAmount", refundAmount)
            args.putDouble("UpdatedBalance", updatedBalance)
            fragment.arguments = args
            return fragment
        }
    }
}
