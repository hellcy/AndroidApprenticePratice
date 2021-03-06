package com.yuan.tafewallet.topup


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
import com.yuan.tafewallet.adapters.TopupCardDetailsTableViewAdapter
import com.yuan.tafewallet.models.PaperCutAccount
import com.yuan.tafewallet.models.WestpacAccount
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_topup_card_details.view.*

class TopupCardDetailsFragment : Fragment() {
    lateinit var account: PaperCutAccount
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
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            activity.supportActionBar?.title = "Card Details"
            activity.nav_view.isVisible = false
        }

        val view = inflater.inflate(R.layout.fragment_topup_card_details, container, false)
        view.AccountBalanceLabel.text = (activity as MainActivity).convertDollarSign(account.Balance)
        view.AccountNameLabel.text = account.AccountName
        view.ContinueButton.setOnClickListener { v ->
            continueButtonPressed() }

        view.topupCardDetailsTable.adapter = TopupCardDetailsTableViewAdapter(westpacAccount)
        view.topupCardDetailsTable.layoutManager = LinearLayoutManager(activity)


        return view
    }

    private fun continueButtonPressed() {
        val fragment = TopupConfirmFragment.newInstance(account, westpacAccount, amount, null)
        (activity as MainActivity).gotoFragment(fragment, TopupConfirmFragment.TAG)
    }

    companion object {
        val TAG = TopupCardDetailsFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: PaperCutAccount, westpacAccount: WestpacAccount, amount: Int): TopupCardDetailsFragment {
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
