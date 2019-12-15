package com.yuan.tafewallet.refund

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.CustomProgressBar
import com.yuan.tafewallet.MainActivity

import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.RefundConfirmTableViewAdapter
import com.yuan.tafewallet.models.WestpacTransaction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_refund_confirm.view.*

class RefundConfirmFragment : Fragment(), RefundConfirmTableViewAdapter.RefundConfirmTableViewClickListener {
    lateinit var transactions: ArrayList<WestpacTransaction>
    var amount: Double = 0.0
    val progressBar = CustomProgressBar()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        transactions = arguments?.getParcelableArrayList<WestpacTransaction>("Transactions")!!
        amount = arguments?.getDouble("Amount")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.title = "Refund"
            activity.nav_view.isVisible = false
        }

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_refund_confirm, container, false)
        view.refundAmount.text = "$" + "%.2f".format(amount)
        view.RefundConfirmButton.setOnClickListener { v ->
            confirmRefundButtonPressed()
        }
        view.progressBar.isVisible = false

        view.refundConfirmTable.adapter = RefundConfirmTableViewAdapter(transactions, this)
        view.refundConfirmTable.layoutManager = LinearLayoutManager(activity)
        return view
    }

    override fun listItemClicked(position: Int) {
        val fragment = RefundCardDetailsFragment.newInstance(transactions[position])
        (activity as MainActivity).gotoFragment(fragment, RefundCardDetailsFragment.TAG)
    }

    private fun confirmRefundButtonPressed() {
        progressBar.show(context!!,"Refund in progress... \nPlease do not close or exit this application")

        Handler().postDelayed({
            //Dismiss progress bar after 4 seconds
            progressBar.dialog.dismiss()
            // TODO: call api to get refundedTransactions, for now use transactions for display only
            // TODO: call api to calculate refunded amount for each transaction, add them up to get total refunded amount
            // TODO: get the updatedBalance from the last transaction in the loop
            // sample data
            var refundedAmount = 5.0
            var updatedBalance = 18.95
            val fragment = RefundCompleteFragment.newInstance(transactions, refundedAmount, amount, updatedBalance)
            (activity as MainActivity).gotoFragment(fragment, RefundCompleteFragment.TAG)
        }, 1000)
    }

    companion object {
        val TAG = RefundConfirmFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(transactions: ArrayList<WestpacTransaction>, amount: Double): RefundConfirmFragment {
            val fragment = RefundConfirmFragment()
            val args = Bundle()
            args.putParcelableArrayList("Transactions", transactions)
            args.putDouble("Amount", amount)
            fragment.arguments = args
            return fragment
        }
    }
}
