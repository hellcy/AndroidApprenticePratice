package com.yuan.tafewallet.history

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuan.tafewallet.DatePickerFragment
import com.yuan.tafewallet.MainActivity
import com.yuan.tafewallet.R
import com.yuan.tafewallet.adapters.HistoryTransactionsTableViewAdapter
import com.yuan.tafewallet.models.PaperCutAccount
import com.yuan.tafewallet.models.Transaction
import com.yuan.tafewallet.service.GetTransactionHistoryRequestBody
import com.yuan.tafewallet.service.GetTransactionHistoryService
import com.yuan.tafewallet.topup.TopupFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_history_transactions.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class HistoryTransactionsFragment : Fragment(), HistoryTransactionsTableViewAdapter.HistoryTransactionsTableViewClickListener {
    private val fromDateCode: Int = 1
    private val toDateCode: Int = 2
    private var selectedDate = ""
    private var fromDate: String? = ""
    private var toDate: String? = ""

    private lateinit var paperCutAccount: PaperCutAccount
    var transactions = ArrayList<Transaction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paperCutAccount = arguments?.getParcelable("Account")!!
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
            activity.supportActionBar?.title = paperCutAccount.AccountName
            activity.nav_view.isVisible = false
        }

        val view = inflater.inflate(R.layout.fragment_history_transactions, container, false)
        view.AccountNameLabel.text = paperCutAccount.AccountName
        view.AccountBalanceLabel.text = (activity as MainActivity).convertDollarSign(paperCutAccount.Balance)

        view.FromDate.setOnClickListener {
            // create the datePickerFragment
            val newFragment: AppCompatDialogFragment = DatePickerFragment()
            // set the targetFragment to receive the results, specifying the request code
            newFragment.setTargetFragment(this, fromDateCode)
            // show the datePicker
            newFragment.show(activity.supportFragmentManager, "datePicker")
        }

        view.ToDate.setOnClickListener {
            val newFragment: AppCompatDialogFragment = DatePickerFragment()
            newFragment.setTargetFragment(this, toDateCode)
            newFragment.show(activity.supportFragmentManager, "datePicker")
        }

        view.SearchButton.setOnClickListener { v ->
            searchButtonPressed()
        }

        view.historyTransactionsTable.adapter = HistoryTransactionsTableViewAdapter(transactions, this)
        view.historyTransactionsTable.layoutManager = LinearLayoutManager(activity)

        return view
    }

    override fun listItemClicked(position: Int) {
        val fragment = HistoryTransactionDetailsFragment.newInstance(transactions[transactions.size - position - 1])
        (activity as MainActivity).gotoFragment(fragment, HistoryTransactionDetailsFragment.TAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val format = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        if (requestCode == fromDateCode && resultCode == Activity.RESULT_OK) { // get date from string
            selectedDate = data!!.getStringExtra("selectedDate")!!
            // set the value of the editText
            view?.FromDate?.text = selectedDate
            view?.FromDate?.setTextColor(Color.BLACK)
            calendar.time = SimpleDateFormat("MM/dd/yyyy").parse(selectedDate)
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            fromDate = format.format(calendar.time)
        }

        if (requestCode == toDateCode && resultCode == Activity.RESULT_OK) { // get date from string
            selectedDate = data!!.getStringExtra("selectedDate")!!
            // set the value of the editText
            view?.ToDate?.text = selectedDate
            view?.ToDate?.setTextColor(Color.BLACK)
            calendar.time = SimpleDateFormat("MM/dd/yyyy").parse(selectedDate)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            toDate = format.format(calendar.time)
        }
    }

    private fun searchButtonPressed() {
        //TODO: call api to get transaction history and pass it to table adapter
        if (view?.FromDate?.text == "FromDate") {
            view?.FromDate?.setTextColor(Color.RED)
        } else if (view?.ToDate?.text == "ToDate") {
            view?.ToDate?.setTextColor(Color.RED)
        } else {
            getTransactionHistory(view!!)
        }
    }

    private fun getTransactionHistory(view: View) {
        val getTransactionHistoryService = GetTransactionHistoryService.instance
        val requestBody = GetTransactionHistoryRequestBody(paperCutAccount.AccountName!!, fromDate!!, toDate!!)
        val request = getTransactionHistoryService.getTransactionHistory(requestBody)

        request.enqueue(object : Callback<ArrayList<Transaction>> {
            override fun onFailure(call: Call<ArrayList<Transaction>>, t: Throwable) {
                Log.i(TopupFragment.TAG, "Call to ${call.request()?.url()} " + "failed with ${t.toString()}")
            }

            override fun onResponse(
                call: Call<ArrayList<Transaction>>,
                response: Response<ArrayList<Transaction>>
            ) {
                Log.i(TopupFragment.TAG, "Got response with status code " + "${response.code()} and message " + response.message())
                if (response.isSuccessful) {
                    if (response.body()?.size == 0) (activity as MainActivity).showAlert()
                    else {
                        transactions = response.body()!!
                        Log.i(TopupFragment.TAG, "get transaction history response body " + transactions)
                        view.historyTransactionsTable.adapter = HistoryTransactionsTableViewAdapter(transactions, this@HistoryTransactionsFragment)
                    }
                } else {
                    (activity as MainActivity).showAlert()
                }
            }
        })
    }

    companion object {
        val TAG = HistoryTransactionsFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(account: PaperCutAccount): HistoryTransactionsFragment {
            val fragment = HistoryTransactionsFragment()
            val args = Bundle()
            args.putParcelable("Account", account)
            fragment.arguments = args
            return fragment
        }
    }

}
