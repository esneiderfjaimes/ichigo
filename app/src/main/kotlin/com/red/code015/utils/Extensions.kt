package com.red.code015.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(text: String?) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Context.toast(@StringRes resId: Int) {
    toast(getString(resId))
}