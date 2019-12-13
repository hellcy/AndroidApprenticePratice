package com.yuan.tafewallet.refund

import android.content.Context
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
import com.yuan.tafewallet.adapters.RefundCompleteTableViewAdapter
import com.yuan.tafewallet.models.PaperCutAccount
import com.yuan.tafewallet.models.PaperCutAccountManager
import com.yuan.tafewallet.models.WestpacTransaction
import com.yuan.tafewallet.topup.TopupFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_refund_complete.view.*

class RefundCompleteFragment : Fragment(), RefundCompleteTableViewAdapter.RefundCompleteTableViewClickListener {
    lateinit var transactions: ArrayList<WestpacTransaction>
    var refundedAmount: Double = 0.0
    var refundAmount: Double = 0.0
    var updatedBalance: Double = 0.0
    lateinit var paperCutAccountManager: PaperCutAccountManager
    lateinit var primaryAccount: PaperCutAccount

    override fun onAttach(context: Context) {
        super.onAttach(context)
        paperCutAccountManager = PaperCutAccountManager(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        transactions = arguments?.getParcelableArrayList<WestpacTransaction>("Transactions")!!
        refundedAmount = arguments?.getDouble("RefundedAmount")!!
        refundAmount = arguments?.getDouble("RefundAmount")!!
        updatedBalance = arguments?.getDouble("UpdatedBalance")!!
        primaryAccount = paperCutAccountManager.readPrimaryAccount()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.title = "Refund Complete"
            activity.nav_view.isVisible = false
        }

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_refund_complete, container, false)
        view.AccountNameLabel.text = primaryAccount.AccountName
        view.AccountBalanceLabel.text = "$" + "%.2f".format(updatedBalance)
        view.refundAmount.text = "$" + "%.2f".format(refundedAmount)
        view.DoneButton.setOnClickListener { v ->
            doneButtonPressed()
        }

        view.refundCompleteTable.adapter = RefundCompleteTableViewAdapter(transactions, this)
        view.refundCompleteTable.layoutManager = LinearLayoutManager(activity)

        return view
    }

    override fun listItemClicked(position: Int) {
        val fragment = RefundTransactionDetailsFragment.newInstance(transactions[position])
        (activity as MainActivity).gotoFragment(fragment, RefundTransactionDetailsFragment.TAG)
    }

    private fun doneButtonPressed() {
        val fragment = RefundFragment.newInstance()
        (activity as MainActivity).clearBackStack()
        (activity as MainActivity).setFragment(fragment)
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
