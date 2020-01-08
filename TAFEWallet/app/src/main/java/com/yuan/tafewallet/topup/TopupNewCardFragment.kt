package com.yuan.tafewallet.topup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.yuan.tafewallet.MainActivity
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.Account
import com.yuan.tafewallet.models.PaperCutAccount
import com.yuan.tafewallet.models.WestpacAccount
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_topup_new_card.view.*

class TopupNewCardFragment : Fragment() {
    lateinit var account: PaperCutAccount
    var amount: Int = 0
    lateinit var secretToken: String
    var westpacAccount = WestpacAccount()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = arguments?.getParcelable("Account")!!
        amount = arguments?.getInt("Amount")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = "New Credit/Debit Card"
            activity.nav_view.isVisible = false
        }

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_topup_new_card, container, false)
        view.AccountBalanceLabel.text = (activity as MainActivity).convertDollarSign(account.Balance)
        view.AccountNameLabel.text = account.AccountName
        view.webView.loadUrl("https://s3-ap-southeast-2.amazonaws.com/paymentapi.westpac.trustedframe/westpacTrustedFrame.html")
        view.webView.settings.javaScriptEnabled = true
        view.webView.settings.saveFormData = false
        view.webView.addJavascriptInterface(WebAppInterface(this), "trustedFrame")
        view.webView.webViewClient = WebViewClient()
        return view
    }

    companion object {
        val TAG = TopupNewCardFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: PaperCutAccount, amount: Int): TopupNewCardFragment {
            val fragment = TopupNewCardFragment()
            val args = Bundle()
            args.putParcelable("Account", account)
            args.putInt("Amount", amount)
            fragment.arguments = args
            return fragment
        }
    }

    class WebAppInterface(val fragment: TopupNewCardFragment) {

        @JavascriptInterface
        fun postMessage(secretToken: String, cardScheme: String, cardType: String, cardNumber: String,
        cardholderName: String, expiryDateMonth: String, expiryDateYear: String) {
            fragment.secretToken = secretToken
            fragment.westpacAccount.cardScheme = cardScheme
            fragment.westpacAccount.cardType = cardType
            fragment.westpacAccount.cardNumber = cardNumber
            fragment.westpacAccount.cardholderName = cardholderName
            fragment.westpacAccount.expiryDateMonth = expiryDateMonth
            fragment.westpacAccount.expiryDateYear = expiryDateYear.takeLast(2)
            fragment.performGoToFragment()
        }

    }

    fun performGoToFragment() {
        val fragment = TopupConfirmFragment.newInstance(account, westpacAccount, amount, secretToken)
        (activity as MainActivity).gotoFragment(fragment, TopupConfirmFragment.TAG)
    }
}
