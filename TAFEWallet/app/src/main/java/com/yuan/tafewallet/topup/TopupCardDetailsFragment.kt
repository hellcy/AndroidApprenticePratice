package com.yuan.tafewallet.topup


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.MainActivity
import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.TopupCardDetailsTableViewAdapter
import com.yuan.tafewallet.models.Account
import com.yuan.tafewallet.models.WestpacAccount
import kotlinx.android.synthetic.main.fragment_topup_card_details.view.*

class TopupCardDetailsFragment : Fragment() {
    lateinit var account: Account
    lateinit var westpacAccount: WestpacAccount
    var amount: Int = 0

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
            activity.supportActionBar?.title = "Card Details"
        }

        val view = inflater.inflate(R.layout.fragment_topup_card_details, container, false)
        view.AccountBalanceLabel.text = account.accountBalance
        view.AccountNameLabel.text = account.accountName
        view.ContinueButton.setOnClickListener { v ->
            continueButtonPressed() }

        view.topupCardDetailsTable.adapter = TopupCardDetailsTableViewAdapter(westpacAccount)
        view.topupCardDetailsTable.layoutManager = LinearLayoutManager(activity)


        return view
    }

    private fun continueButtonPressed() {
        val fragment = TopupConfirmFragment.newInstance(account, westpacAccount, amount)
        (activity as MainActivity).gotoFragment(fragment, TopupConfirmFragment.TAG)
    }

    companion object {
        val TAG = TopupCardDetailsFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: Account, westpacAccount: WestpacAccount, amount: Int): TopupCardDetailsFragment {
            val fragment = TopupCardDetailsFragment()
            val args = Bundle()
            args.putParcelable("Account", account)
            args.putParcelable("WestpacAccount", westpacAccount)
            args.putInt("Amount", amount)
            fragment.arguments = args
            return fragment
        }
    }
}
