package com.example.parkswitchapp.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import io.socket.client.IO
import io.socket.emitter.Emitter
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import kotlin.concurrent.thread


class APIUtil {
    companion object {
        private const val url = "http://192.168.100.2:3000/"
        private val client = OkHttpClient()
        private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

        private val webSocket = IO.socket(url);

        init {
            webSocket.connect();
        }

        fun getRequest(endpoint: String, callback: (result: JSONObject) -> Any) {
            thread {
                val request = Request.Builder().url(url + endpoint).get().build()
                val response = client.newCall(request).execute()
                callback(JSONObject(response.body!!.string()))
            }
        }

        fun postRequest(endpoint: String, body: JSONObject, callback: (result: JSONObject) -> Any) {
            thread {
                val requestBody = body.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder().url(url + endpoint).post(requestBody).build()
                val response = client.newCall(request).execute()
                callback(JSONObject(response.body!!.string()))
            }
        }

        fun on(eventName: String, callback: (payload: JSONObject) -> Any) {
            webSocket.on(eventName) {
                webSocket.off(eventName) // will be triggered one-time
                callback(it[0] as JSONObject)
            }
        }

    }
}