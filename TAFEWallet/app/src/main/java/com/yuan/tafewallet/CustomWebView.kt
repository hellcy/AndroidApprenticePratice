package com.yuan.tafewallet

import android.content.Context
import android.util.AttributeSet
import android.view.inputmethod.BaseInputConnection
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.webkit.WebView
import java.util.jar.Attributes

class CustomWebView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : WebView(context, attrs, defStyleAttr) {
//    override fun onCreateInputConnection(outAttrs: EditorInfo?): InputConnection {
//        val inputConnection = BaseInputConnection(this, false)
//        outAttrs?.imeOptions = EditorInfo.IME_ACTION_DONE
//        outAttrs?.inputType = EditorInfo.TYPE_CLASS_NUMBER
//        return inputConnection
//    }
}