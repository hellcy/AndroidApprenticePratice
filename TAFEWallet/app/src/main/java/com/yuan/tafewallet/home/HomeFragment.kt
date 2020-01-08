package com.yuan.tafewallet.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.PopupTermsFragment
import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.HomeTableViewAdapter
import com.yuan.tafewallet.models.PaperCutAccountManager
import com.yuan.tafewallet.models.UnicardAccountManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {
    private lateinit var unicardAccountManager: UnicardAccountManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        unicardAccountManager = UnicardAccountManager(context)
    }

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

        val paperCutAccountManager = PaperCutAccountManager(context!!)

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        view.viewTermsLabel.setOnClickListener {
            showTermsFragment()
        }
        view.Username.text = paperCutAccountManager.readPrimaryAccount().FullName
        view.homeTableView.adapter = HomeTableViewAdapter(context!!)
        view.homeTableView.layoutManager = LinearLayoutManager(activity)

        if (unicardAccountManager.readUnicardAccount().TermAndConditionAgreementAt == null) {
            showTermsFragment()
        } else {
            view.termsLabel.text = getString(R.string.terms_label, unicardAccountManager.readUnicardAccount().TermAndConditionAgreementAt)
        }
        return view
    }

    private fun showTermsFragment() {
        val newFragment = PopupTermsFragment()
        val dialogFragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        val prev = activity!!.supportFragmentManager.findFragmentByTag("dialog")
        if (prev != null) {
            dialogFragmentTransaction.remove(prev)
        }
        dialogFragmentTransaction.addToBackStack(null)
        newFragment.show(dialogFragmentTransaction, "dialog")
    }

    companion object {
        fun newInstance(): HomeFragment {
            val homeFragment = HomeFragment()
            val args = Bundle()
            homeFragment.arguments = args
            return homeFragment
        }
    }
}