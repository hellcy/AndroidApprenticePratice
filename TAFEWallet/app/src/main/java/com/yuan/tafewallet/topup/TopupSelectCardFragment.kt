package com.yuan.tafewallet.topup

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.MainActivity

import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.TopupSelectCardTableViewAdapter
import com.yuan.tafewallet.models.Account
import com.yuan.tafewallet.models.WestpacAccount
import com.yuan.tafewallet.models.WestpacAccountManager
import kotlinx.android.synthetic.main.fragment_topup_select_card.view.*

class TopupSelectCardFragment : Fragment(), TopupSelectCardTableViewAdapter.TopupSelectCardTableViewClickListener {
    lateinit var account: Account
    var amount: Int = 0
    private var westpacAccount = WestpacAccount("CREDIT_CARD", "TAFEOP353887960", true,"444433...111","03",
        "22","VISA","CREDIT","yuan","444433...1111")
    var westpacAccounts = ArrayList<WestpacAccount>()
    lateinit var savedAccounts: ArrayList<WestpacAccount>
    lateinit var westpacAccountManager: WestpacAccountManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        westpacAccountManager = WestpacAccountManager(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = arguments?.getParcelable("Account")!!
        amount = arguments?.getInt("Amount")!!
        westpacAccounts.add(westpacAccount)
        westpacAccountManager.saveWestpacAccount(westpacAccounts)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.title = "Select Credit/Debit Card"
        }
        savedAccounts = westpacAccountManager.readWestpacAccounts()

        val view = inflater.inflate(R.layout.fragment_topup_select_card, container, false)
        view.AccountNameLabel.text = account.accountName
        view.AccountBalanceLabel.text = account.accountBalance

        view.topupSelectCardTable.adapter = TopupSelectCardTableViewAdapter(savedAccounts, this)
        view.topupSelectCardTable.layoutManager = LinearLayoutManager(activity)


        return view
    }

    override fun listItemClicked(position: Int) {
        val fragment = TopupCardDetailsFragment.newInstance(account, savedAccounts[position], amount)
        (activity as MainActivity).gotoFragment(fragment, TopupCardDetailsFragment.TAG)
    }

    companion object {
        val TAG = TopupSelectCardFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: Account, amount: Int): TopupSelectCardFragment {
            val fragment = TopupSelectCardFragment()
            val args = Bundle()
            args.putParcelable("Account", account)
            args.putInt("Amount", amount)
            fragment.arguments = args
            return fragment
        }
    }
}
