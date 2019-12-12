package com.yuan.tafewallet.refund

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.yuan.tafewallet.MainActivity
import com.yuan.tafewallet.R
import com.yuan.tafewallet.history.HistoryTransactionsFragment
import com.yuan.tafewallet.models.*
import com.yuan.tafewallet.topup.TopupFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_refund.view.*

class RefundFragment : Fragment() {
    lateinit var paperCutAccountManager: PaperCutAccountManager
    lateinit var primaryAccount: PaperCutAccount
    var refundAmount: Double = 0.0
    var westpacTransactions = ArrayList<WestpacTransaction>()

    // sample data
    lateinit var westpacTransaction: WestpacTransaction
    lateinit var money: Money
    lateinit var creditCard: CreditCard

    override fun onAttach(context: Context) {
        super.onAttach(context)
        paperCutAccountManager = PaperCutAccountManager(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        money = Money("AUD", 10.0)
        creditCard = CreditCard("VISA", "CREDIT", "424242...242", "Yuan Test", "09", "22", "TAFE12345678")
        westpacTransaction = WestpacTransaction("PAYMENT", "DateTime", "Approved", "123456", money, creditCard, "5", null)
        westpacTransactions.add(westpacTransaction)
    }

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

        primaryAccount = paperCutAccountManager.readPrimaryAccount()

        val view = inflater.inflate(R.layout.fragment_refund, container, false)
        view.AccountBalanceLabel.text = "$" + "%.2f".format(primaryAccount.Balance)
        view.AccountNameLabel.text = primaryAccount.AccountName
        view.ErrorMessage.isVisible = false
        view.RefundAmount.setOnFocusChangeListener { view, b ->
            if (!b) {
                hideKeyboard(view)
            }
        }
        view.ContinueButton.setOnClickListener { v ->
            continueButtonPressed()
        }

        return view
    }

    private fun continueButtonPressed() {
        refundAmount = view!!.RefundAmount.text.toString().toDoubleOrNull() ?: 0.0
        if (refundAmount <= 0) {
            view!!.ErrorMessage.text = "Please enter a valid amount"
            view!!.ErrorMessage.isVisible = true
        } else if (refundAmount > primaryAccount.Balance) {
            view!!.ErrorMessage.text = "Insufficient balance in account"
            view!!.ErrorMessage.isVisible = true
        } else {
            view!!.ErrorMessage.text = ""
            view!!.ErrorMessage.isVisible = false
            //TODO: call api and go to new fragment
            val fragment = RefundConfirmFragment.newInstance(westpacTransactions, refundAmount)
            (activity as MainActivity).gotoFragment(fragment, HistoryTransactionsFragment.TAG)
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    companion object {
        val TAG = RefundFragment::class.java.simpleName
        @JvmStatic
        fun newInstance() = RefundFragment()
    }
}