package com.yuan.tafewallet.topup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.MainActivity
import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.TopupSelectAmountTableViewAdapter
import com.yuan.tafewallet.models.Account
import com.yuan.tafewallet.models.PaperCutAccount
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_topup_select_amount.view.*


class TopupSelectAmountFragment : Fragment(), TopupSelectAmountTableViewAdapter.TopupSelectAmountTableViewClickListener {
    lateinit var account: PaperCutAccount

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = arguments?.getParcelable("Account")!!
    }

    // initialize all view elements here
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = "Top Up Account"
            activity.nav_view.isVisible = false
        }

        val view = inflater.inflate(R.layout.fragment_topup_select_amount, container, false)
        view.AccountNameLabel.text = account.AccountName
        view.AccountBalanceLabel.text = (activity as MainActivity).convertDollarSign(account.Balance)

        view.topupSelectAmountTable.adapter = TopupSelectAmountTableViewAdapter(this)
        view.topupSelectAmountTable.layoutManager = LinearLayoutManager(activity)


        return view
    }

    override fun listItemClicked(amount: Int) {
        val fragment = TopupSelectCardFragment.newInstance(account, amount)
        (activity as MainActivity).gotoFragment(fragment, TopupSelectCardFragment.TAG)
    }

    companion object {
        val TAG = TopupSelectAmountFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: PaperCutAccount): TopupSelectAmountFragment {
            val fragment = TopupSelectAmountFragment()
            val args = Bundle()
            args.putParcelable("Account", account)
            fragment.arguments = args
            return fragment
        }
    }
}
