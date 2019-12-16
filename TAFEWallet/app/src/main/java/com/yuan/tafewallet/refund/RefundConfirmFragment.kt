package com.yuan.tafewallet.refund

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.CustomProgressBar
import com.yuan.tafewallet.MainActivity

import com.yuan.tafewallet.R
import com.yuan.tafewallet.TAFEWalletApplication
import com.yuan.tafewallet.adapters.RefundConfirmTableViewAdapter
import com.yuan.tafewallet.history.HistoryTransactionsFragment
import com.yuan.tafewallet.models.PaperCutAccountManager
import com.yuan.tafewallet.models.UnicardAccountManager
import com.yuan.tafewallet.models.WestpacTransaction
import com.yuan.tafewallet.service.GetRefundTransactionService
import com.yuan.tafewallet.service.GetRefundTransactionsRequestBody
import com.yuan.tafewallet.service.RefundTransactionRequestBody
import com.yuan.tafewallet.service.RefundTransactionService
import com.yuan.tafewallet.topup.TopupCompleteFragment
import com.yuan.tafewallet.topup.TopupFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_refund.view.*
import kotlinx.android.synthetic.main.fragment_refund_confirm.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RefundConfirmFragment : Fragment(), RefundConfirmTableViewAdapter.RefundConfirmTableViewClickListener {
    @Inject
    lateinit var mContext: Context
    lateinit var transactions: ArrayList<WestpacTransaction>
    var refundedTransactions = ArrayList<WestpacTransaction>()
    var refundedAmount: Double = 0.0
    var updatedBalance: Double = 0.0

    lateinit var unicardAccountManager: UnicardAccountManager
    lateinit var paperCutAccountManager: PaperCutAccountManager
    var amount: Double = 0.0
    val progressBar = CustomProgressBar()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        unicardAccountManager = UnicardAccountManager(context)
        paperCutAccountManager = PaperCutAccountManager(context)
        (activity?.application as TAFEWalletApplication).mainComponent.injectRefundConfirmFragment(this)

    }

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
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = "Refund"
            activity.nav_view.isVisible = false
        }

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_refund_confirm, container, false)
        view.refundAmount.text = "$" + "%.2f".format(amount)
        view.RefundConfirmButton.setOnClickListener { v ->
            confirmRefundButtonPressed(view)
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

    private fun confirmRefundButtonPressed(view: View) {
        progressBar.show(context!!,"Refund in progress... \nPlease do not close or exit this application")
        var count = transactions.size
        for (transaction in transactions) {
            refundTransaction(view, transaction, count)
            count--
        }

//        Handler().postDelayed({
//            //Dismiss progress bar after 4 seconds
//            progressBar.dialog.dismiss()
//            // TODO: call api to get refundedTransactions, for now use transactions for display only
//            // TODO: call api to calculate refunded amount for each transaction, add them up to get total refunded amount
//            // TODO: get the updatedBalance from the last transaction in the loop
//            // sample data
//            var refundedAmount = 5.0
//            var updatedBalance = 18.95
//            val fragment = RefundCompleteFragment.newInstance(transactions, refundedAmount, amount, updatedBalance)
//            (activity as MainActivity).gotoFragment(fragment, RefundCompleteFragment.TAG)
//        }, 1000)
    }

    private fun refundTransaction(view: View, transaction: WestpacTransaction, count: Int) {
        val refundTransactionService = RefundTransactionService.instance
        val requestBody = RefundTransactionRequestBody(transaction.receiptNumber, transaction.refundableAmount.toString(), unicardAccountManager.readUnicardAccount().UnicardID, paperCutAccountManager.readPrimaryAccount().AccountName!!)
        val request = refundTransactionService.refundTransaction(requestBody)

        request.enqueue(object : Callback<WestpacTransaction> {
            override fun onFailure(call: Call<WestpacTransaction>, t: Throwable) {
                Log.i(TopupFragment.TAG, "Call to ${call?.request()?.url()} " + "failed with ${t.toString()}")
            }

            override fun onResponse(
                call: Call<WestpacTransaction>,
                response: Response<WestpacTransaction>
            ) {
                Log.i(TopupFragment.TAG, "Got response with status code " + "${response.code()} and message " + response.message())
                if (response.isSuccessful) {
                    refundedTransactions.add(response.body()!!)
                    refundedAmount += response.body()!!.refundableAmount?.toDouble()!!
                    updatedBalance = response.body()!!.updatedBalance?.toDouble()!!
                    Log.i(TopupFragment.TAG, "refund transaction response body " + "${response.body()}")
                } else {
                    (activity as MainActivity).showAlert()
                }
                if (count == 1) {
                    if (this@RefundConfirmFragment != null && this@RefundConfirmFragment.isVisible) {
                        progressBar.dialog.dismiss()
                        val fragment = RefundCompleteFragment.newInstance(transactions, refundedAmount, amount, updatedBalance)
                        (activity as MainActivity).gotoFragment(fragment, RefundCompleteFragment.TAG)
                    } else {
                        Toast.makeText(mContext, "Refund succeeded", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
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
