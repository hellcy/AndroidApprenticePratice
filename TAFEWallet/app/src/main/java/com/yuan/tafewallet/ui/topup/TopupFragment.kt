package com.yuan.tafewallet.ui.topup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.yuan.tafewallet.R

class TopupFragment : Fragment() {

    private lateinit var topupViewModel: TopupViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        topupViewModel =
            ViewModelProviders.of(this).get(TopupViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_topup, container, false)
        topupViewModel.text.observe(this, Observer {
        })
        return root
    }
}