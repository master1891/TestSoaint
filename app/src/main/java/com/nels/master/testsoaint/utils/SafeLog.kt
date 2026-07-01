package com.nels.master.testsoaint.utils

import android.util.Log

object SafeLog {
    fun e(tag: String, message: String) {
        runCatching { Log.e(tag, message) }
    }

    fun w(tag: String, message: String) {
        runCatching { Log.w(tag, message) }
    }

    fun d(tag: String, message: String) {
        runCatching { Log.d(tag, message) }
    }
}
