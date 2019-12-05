package com.yuan.tafewallet.ui.refund

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yuan.tafewallet.R

class RefundFragment : Fragment() {

    private lateinit var refundViewModel: RefundViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        refundViewModel =
            ViewModelProviders.of(this).get(RefundViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_refund, container, false)
        val textView: TextView = root.findViewById(R.id.text_refund)
        refundViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}