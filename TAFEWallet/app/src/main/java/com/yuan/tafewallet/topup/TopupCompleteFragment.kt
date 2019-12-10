package com.yuan.tafewallet.topup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.MainActivity

import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.TopupTransactionCompleteTableViewAdapter
import com.yuan.tafewallet.models.*
import kotlinx.android.synthetic.main.fragment_topup_complete.view.*



class TopupCompleteFragment : Fragment() {
    lateinit var account: Account
    var amount: Int = 0

    // sample data
    lateinit var westpacTransaction: WestpacTransaction
    lateinit var money: Money
    lateinit var creditCard: CreditCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        account = arguments?.getParcelable("Account")!!
        amount = arguments?.getInt("Amount")!!

        money = Money("AUD", 10.0)
        creditCard = CreditCard("VISA", "CREDIT", "424242...242", "Yuan Test", "09", "22", "TAFE12345678")
        westpacTransaction = WestpacTransaction("PAYMENT", "DateTime", "Approved", "123456", money, creditCard, null, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.title = "Transaction Complete"
            //activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        }

        val view = inflater.inflate(R.layout.fragment_topup_complete, container, false)
        view.AccountNameLabel.text = account.accountName
        view.AccountBalanceLabel.text = account.accountBalance
        view.DoneButton.setOnClickListener { v ->
            doneButtonPressed() }

        view.topupTransactionCompleteTable.adapter = TopupTransactionCompleteTableViewAdapter(westpacTransaction)
        view.topupTransactionCompleteTable.layoutManager = LinearLayoutManager(activity)

        return view
    }

    private fun doneButtonPressed() {
        val fm = activity!!.supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
        val fragment = TopupFragment.newInstance()
        (activity as MainActivity).gotoFragment(fragment, TopupFragment.TAG)
    }

    companion object {
        val TAG = TopupCompleteFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: Account, amount: Int): TopupCompleteFragment {
            val fragment = TopupCompleteFragment()
            val args = Bundle()
            args.putParcelable("Account", account)
            args.putInt("Amount", amount)
            fragment.arguments = args
            return fragment
        }
    }
}
