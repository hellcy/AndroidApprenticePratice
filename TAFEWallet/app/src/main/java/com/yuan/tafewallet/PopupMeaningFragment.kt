package com.yuan.tafewallet

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.fragment_popup_meaning.view.*

class PopupMeaningFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_popup_meaning, container, false)
        view.GotItButton.setOnClickListener { v ->
            gotItButtonPressed()
        }
        return view
    }

    private fun gotItButtonPressed() {
        activity?.onBackPressed()
    }
}
