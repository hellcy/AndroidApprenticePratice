package com.yuan.tafewallet

import android.app.Dialog
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_popup_terms.view.*


class PopupTermsFragment : DialogFragment() {
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
        return view
    }

    private fun acceptButtonPressed() {
        // TODO: update termsAgress time onClick
        activity?.onBackPressed()
    }

    private fun declineButtonPressed() {
        activity?.onBackPressed()
    }

}
