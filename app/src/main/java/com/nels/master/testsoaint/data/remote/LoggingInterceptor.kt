package com.nels.master.testsoaint.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer

class LoggingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body
        val isLogin = request.url.encodedPath.contains("auth/login", ignoreCase = true)

        Log.d(TAG, "═══════════════════════════════════════════")
        Log.d(TAG, "→ ${request.method} ${request.url}")

        if (request.headers.size > 0) {
            Log.d(TAG, "── HEADERS ──")
            request.headers.forEach { header ->
                Log.d(TAG, "  ${header.first}: ${header.second}")
            }
        }

        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            val contentType = requestBody.contentType()
            val bodyString = buffer.readUtf8()

            if (isPlainText(contentType)) {
                Log.d(TAG, "── BODY ──")
                if (isLogin) {
                    Log.d(TAG, REDACTED)
                } else {
                    Log.d(TAG, bodyString)
                }
            } else {
                Log.d(TAG, "  [binary body: ${requestBody.contentLength()} bytes]")
            }
        }

        Log.d(TAG, "───")

        val startMs = System.currentTimeMillis()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            Log.e(TAG, "✕ Request failed: ${e.message}")
            throw e
        }
        val durationMs = System.currentTimeMillis() - startMs

        val responseBody = response.body
        Log.d(TAG, "← ${response.code} ${response.message} (${durationMs}ms)")

        if (responseBody != null && isPlainText(responseBody.contentType())) {
            val bodyString = responseBody.string()
            Log.d(TAG, "── RESPONSE BODY ──")
            if (isLogin) {
                Log.d(TAG, REDACTED)
            } else {
                val maxLen = 3800
                if (bodyString.length > maxLen) {
                    bodyString.chunked(maxLen).forEach { chunk ->
                        Log.d(TAG, chunk)
                    }
                } else {
                    Log.d(TAG, bodyString)
                }
            }

            val newResponse = response.newBuilder()
                .body(bodyString.toResponseBody(responseBody.contentType()))
                .build()
            Log.d(TAG, "═══════════════════════════════════════════")
            return newResponse
        }

        Log.d(TAG, "  [${responseBody?.contentLength() ?: 0} bytes]")
        Log.d(TAG, "═══════════════════════════════════════════")
        return response
    }

    private fun isPlainText(contentType: MediaType?): Boolean {
        val type = contentType?.type ?: return false
        val subtype = contentType.subtype ?: return false
        return (type == "text" || (type == "application" &&
                (subtype == "json" || subtype == "xml" || subtype == "x-www-form-urlencoded")))
    }
}

private const val TAG = "HTTP"
private const val REDACTED = "[credentials redacted]"
