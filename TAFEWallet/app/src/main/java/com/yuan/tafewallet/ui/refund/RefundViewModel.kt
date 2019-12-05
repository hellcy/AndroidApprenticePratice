package com.yuan.tafewallet.ui.refund

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RefundViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Refund Fragment"
    }
    val text: LiveData<String> = _text
}