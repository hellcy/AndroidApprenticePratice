package com.yuan.tafewallet.topup

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import com.yuan.tafewallet.adapters.TopupCardDetailsTableViewAdapter
import com.yuan.tafewallet.models.Account
import com.yuan.tafewallet.models.WestpacAccount
import kotlinx.android.synthetic.main.fragment_topup_confirm.view.*
import android.text.style.UnderlineSpan
import android.text.SpannableString
import androidx.core.view.isVisible
import com.yuan.tafewallet.MainActivity
import kotlinx.android.synthetic.main.activity_main.*


class TopupConfirmFragment : Fragment() {
    lateinit var account: Account
    lateinit var westpacAccount: WestpacAccount
    var amount: Int = 0
    var saveCard: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = arguments?.getParcelable("Account")!!
        westpacAccount = arguments?.getParcelable("WestpacAccount")!!
        amount = arguments?.getInt("Amount")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.title = "Confirm Top Up"
            activity.nav_view.isVisible = false
        }

        val view = inflater.inflate(com.yuan.tafewallet.R.layout.fragment_topup_confirm, container, false)
        view.AccountNameLabel.text = account.accountName
        view.AccountBalanceLabel.text = account.accountBalance
        view.Amount.text = "$" + "%.2f".format(amount.toDouble())

        val text = "What does it mean?"
        val content = SpannableString(text)
        content.setSpan(UnderlineSpan(), 0, text.length, 0)
        view.meaningLabel.setText(content)
        saveCard = view.saveCardBox.isChecked
        view.ConfirmButton.setOnClickListener { v ->
            confirmButtonPressed() }

        view.topupConfirmCardDetailsTable.adapter = TopupCardDetailsTableViewAdapter(westpacAccount)
        view.topupConfirmCardDetailsTable.layoutManager = LinearLayoutManager(activity)
        return view
    }

    private fun confirmButtonPressed() {
        // TODO: use saveCard boolean
        val fragment = TopupCompleteFragment.newInstance(account, amount)
        (activity as MainActivity).gotoFragment(fragment, TopupCompleteFragment.TAG)
    }

    companion object {
        val TAG = TopupConfirmFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: Account, westpacAccount: WestpacAccount, amount: Int, secretToken: String?): TopupConfirmFragment {
            val fragment = TopupConfirmFragment()
            val args = Bundle()
            args.putParcelable("Account", account)
            args.putParcelable("WestpacAccount", westpacAccount)
            args.putInt("Amount", amount)
            fragment.arguments = args
            return fragment
        }
    }
}
