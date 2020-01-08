package com.yuan.tafewallet.refund

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.yuan.tafewallet.CustomProgressBar
import com.yuan.tafewallet.MainActivity
import com.yuan.tafewallet.R
import com.yuan.tafewallet.TAFEWalletApplication
import com.yuan.tafewallet.history.HistoryTransactionsFragment
import com.yuan.tafewallet.models.*
import com.yuan.tafewallet.service.GetRefundTransactionService
import com.yuan.tafewallet.service.GetRefundTransactionsRequestBody
import com.yuan.tafewallet.topup.TopupFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_refund.view.*
import kotlinx.android.synthetic.main.fragment_refund.view.AccountBalanceLabel
import kotlinx.android.synthetic.main.fragment_refund.view.AccountNameLabel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class RefundFragment : Fragment() {
    @Inject
    lateinit var mContext: Context
    private lateinit var unicardAccountManager: UnicardAccountManager
    lateinit var paperCutAccountManager: PaperCutAccountManager
    lateinit var primaryAccount: PaperCutAccount
    var refundAmount: Double = 0.0
    var westpacTransactions = ArrayList<WestpacTransaction>()
    val progressBar = CustomProgressBar()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        unicardAccountManager = UnicardAccountManager(context)
        paperCutAccountManager = PaperCutAccountManager(context)
        (activity?.application as TAFEWalletApplication).mainComponent.injectRefundFragment(this)
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
        view.AccountBalanceLabel.text = (activity as MainActivity).convertDollarSign(primaryAccount.Balance)
        view.AccountNameLabel.text = primaryAccount.AccountName
        view.ErrorMessage.isVisible = false
        view.RefundAmount.setOnFocusChangeListener { view, b ->
            if (!b) {
                hideKeyboard(view)
            }
        }
        view.ContinueButton.setOnClickListener { v ->
            continueButtonPressed(view)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        view?.RefundAmount?.text?.clear()
    }

    private fun continueButtonPressed(view: View) {
        refundAmount = view.RefundAmount.text.toString().toDoubleOrNull() ?: 0.0
        if (refundAmount < 2.5) {
            view.ErrorMessage.text = "Please enter a valid amount"
            view.ErrorMessage.isVisible = true
        } else if (refundAmount > primaryAccount.Balance) {
            view.ErrorMessage.text = "Insufficient balance in account"
            view.ErrorMessage.isVisible = true
        } else {
            view.ErrorMessage.text = ""
            view.ErrorMessage.isVisible = false
            //TODO: call api and go to new fragment
            view.ContinueButton.isEnabled = false
            progressBar.show(context!!,"Getting refund transactions... \nPlease do not close or exit this application")
            getRefundTransactions(view)
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getRefundTransactions(view: View) {
        val getRefundTransactionsService = GetRefundTransactionService.instance
        val requestBody = GetRefundTransactionsRequestBody(unicardAccountManager.readUnicardAccount().UnicardID, refundAmount.toString())
        val request = getRefundTransactionsService.getRefundTransactions(requestBody)

        request.enqueue(object : Callback<ArrayList<WestpacTransaction>> {
            override fun onFailure(call: Call<ArrayList<WestpacTransaction>>, t: Throwable) {
                Log.i(TopupFragment.TAG, "Call to ${call.request()?.url()} " + "failed with $t")
            }

            override fun onResponse(
                call: Call<ArrayList<WestpacTransaction>>,
                response: Response<ArrayList<WestpacTransaction>>
            ) {
                progressBar.dialog.dismiss()
                Log.i(TopupFragment.TAG, "Got response with status code " + "${response.code()} and message " + response.message())
                if (response.isSuccessful) {
                    if (!this@RefundFragment.isVisible) {
                        Toast.makeText(mContext, "Get refund transactions cancelled", Toast.LENGTH_LONG).show()
                    }
                    else if (response.body()?.size == 0) (activity as MainActivity).showAlert()
                    else {
                        progressBar.dialog.dismiss()
                        westpacTransactions = response.body()!!
                        Log.i(TopupFragment.TAG, "get refund transactions response body " + westpacTransactions)
                        val fragment = RefundConfirmFragment.newInstance(westpacTransactions, refundAmount)
                        (activity as MainActivity).gotoFragment(fragment, HistoryTransactionsFragment.TAG)
                    }
                } else {
                    (activity as MainActivity).showAlert()
                }
                view.ContinueButton.isEnabled = true
            }
        })
    }

    companion object {
        val TAG = RefundFragment::class.java.simpleName
        @JvmStatic
        fun newInstance() = RefundFragment()
    }
}