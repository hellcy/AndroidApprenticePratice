package com.yuan.tafewallet.ui.topup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.Account


class TopupSelectAmountFragment : Fragment() {
    lateinit var account: Account
    lateinit var tableView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        account = arguments?.getParcelable("Account")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.actionBar?.show()
        return inflater.inflate(R.layout.fragment_topup_select_amount, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.let {
            tableView = it.findViewById(R.id.topupSelectAmountTable)
            tableView.layoutManager = LinearLayoutManager(activity)
            tableView.adapter = TopupSelectAmountTableViewAdapter(this)

        }
    }

    override fun onResume() {
        super.onResume()
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.supportActionBar!!.show()
        }
    }

    companion object {
        val TAG = TopupSelectAmountFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: Account): TopupSelectAmountFragment {
            val fragment = TopupSelectAmountFragment()
            val args = Bundle()
            args.putParcelable("Account", account)
            fragment.arguments = args
            return fragment
        }
    }
}
