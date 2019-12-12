package com.yuan.tafewallet.topup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuan.tafewallet.MainActivity
import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.TopupSelectAccountTableViewAdapter
import com.yuan.tafewallet.models.Account
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_topup.view.*

class TopupFragment : Fragment(), TopupSelectAccountTableViewAdapter.TopupSelectAccountTableViewClickListener {
    // initialize all elements here
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

        val view = inflater.inflate(R.layout.fragment_topup, container, false)
        view.TopUpAccountTable.adapter = TopupSelectAccountTableViewAdapter(this)
        view.TopUpAccountTable.layoutManager = LinearLayoutManager(activity)
        return view
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