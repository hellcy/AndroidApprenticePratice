package com.yuan.tafewallet.ui.topup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.MainActivity
import com.yuan.tafewallet.R
import com.yuan.tafewallet.models.Account

class TopupFragment : Fragment(), TopupSelectAccountTableViewAdapter.TopupSelectAccountTableViewClickListener {

    private lateinit var topupViewModel: TopupViewModel
    lateinit var tableView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_topup, container, false)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.let {
            tableView = it.findViewById(R.id.TopUpAccountTable)
            tableView.layoutManager = LinearLayoutManager(activity)
            tableView.adapter = TopupSelectAccountTableViewAdapter(this)

        }
    }

    override fun listItemClicked(account: Account) {
        val fragment = TopupSelectAmountFragment.newInstance(account)
        (activity as MainActivity).gotoFragment(fragment, TopupSelectAmountFragment.TAG)
    }

    companion object {
        val TAG = TopupFragment::class.java.simpleName
        @JvmStatic
        fun newInstance() = TopupFragment()
    }
}