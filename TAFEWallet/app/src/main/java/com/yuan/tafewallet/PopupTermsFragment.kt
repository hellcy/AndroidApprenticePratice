package com.yuan.tafewallet

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.yuan.tafewallet.models.PaperCutAccountManager
import com.yuan.tafewallet.models.UnicardAccount
import com.yuan.tafewallet.models.UnicardAccountManager
import com.yuan.tafewallet.service.UpdateUnicardAccountRequestBody
import com.yuan.tafewallet.service.UpdateUnicardAccountService
import kotlinx.android.synthetic.main.fragment_popup_terms.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class PopupTermsFragment : DialogFragment() {
    lateinit var unicardAccountManager: UnicardAccountManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        unicardAccountManager = UnicardAccountManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_popup_terms, container, false)
        view.AcceptButton.setOnClickListener { v ->
            acceptButtonPressed()
        }
        view.DeclineButton.setOnClickListener { v ->
            declineButtonPressed()
        }
        // TODO: check termsAgree time and hide decline button
        if (unicardAccountManager.readUnicardAccount().TermAndConditionAgreementAt != null) {
            view.DeclineButton.isVisible = false
            view.AcceptButton.text = "Close"
        }
        return view
    }

    private fun acceptButtonPressed() {
        // TODO: update termsAgress time onClick
        if (unicardAccountManager.readUnicardAccount().TermAndConditionAgreementAt == null) {
            updateUnicardAccount()
        } else {
            activity?.onBackPressed()
        }
    }

    private fun declineButtonPressed() {
        val intent = Intent(activity,LoginActivity::class.java)
        startActivity(intent)
    }

    private fun updateUnicardAccount() {
        val format = SimpleDateFormat("dd/MM/yyyy hh:mm aaa")
        val updateUnicardAccountService = UpdateUnicardAccountService.instance
        val requestBody = UpdateUnicardAccountRequestBody(
            unicardAccountManager.readUnicardAccount().UnicardID,
            unicardAccountManager.readUnicardAccount().PaperCutID,
            unicardAccountManager.readUnicardAccount().QuickStreamID!!,
            unicardAccountManager.readUnicardAccount().Email,
            format.format(Calendar.getInstance().time))
        val request = updateUnicardAccountService.updateUnicardAccount(requestBody)

        request.enqueue(object : Callback<ArrayList<UnicardAccount>> {
            override fun onFailure(call: Call<ArrayList<UnicardAccount>>, t: Throwable) {
                Log.i("TermsFragment", "Call to ${call?.request()?.url()} " + "failed with ${t.toString()}")
            }

            override fun onResponse(
                call: Call<ArrayList<UnicardAccount>>,
                response: Response<ArrayList<UnicardAccount>>
            ) {
                Log.i("TermsFragment", "Got response with status code " + "${response?.code()} and message " + "${response?.message()}")
                if (response.isSuccessful) {
                    if (response?.body()?.size == 0) (activity as MainActivity).showAlert()
                    else {
                        unicardAccountManager.saveUnicardAccount(response?.body()!![0]) // save to global objects
                        Log.i("TermsFragment", "update Unicard Account response body " + "${unicardAccountManager.readUnicardAccount()}")
                        unicardAccountManager.savePaperCutID(unicardAccountManager.readUnicardAccount().PaperCutID)
                    }
                } else {
                    (activity as MainActivity).showAlert()
                }
            }
        })
    }

}
