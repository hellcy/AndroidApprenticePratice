package com.yuan.tafewallet.ui.topup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TopupViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Topup Fragment"
    }
    val text: LiveData<String> = _text
}