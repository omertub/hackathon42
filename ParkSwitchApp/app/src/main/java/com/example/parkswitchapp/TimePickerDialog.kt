package com.example.parkswitchapp

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkswitchapp.utils.APIUtil
import com.example.parkswitchapp.utils.UserData
import org.json.JSONObject
import java.util.*


class TimePickerDialog(context: Activity) : Dialog(context){

    val appContext = context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.time_picker_dialog)

        val picker : NumberPicker = findViewById(R.id.PickerTime)
        picker.maxValue = 15
        picker.value = 5
        picker.wrapSelectorWheel = false

        findViewById<Button>(R.id.ButtonConfirm).setOnClickListener {
            val date = Date()
            val expirationTime = date.time + picker.value * 60 * 1000

            Toast.makeText(this.context, "$expirationTime", Toast.LENGTH_SHORT).show()

            APIUtil.postRequest("setExpirationTime", JSONObject()
                .put("id", UserData.id)
                .put("expirationTime", expirationTime)
            ) {
                APIUtil.on("parkingCommitted") {
                    this.appContext.runOnUiThread {
                        if (UserData.id == it.get("id") as Int) {
                            Toast.makeText(this.appContext, "parkingCommitted!", Toast.LENGTH_LONG).show()
                        }
                    }
                    APIUtil.clean("parkingCommitted")
                }

                dismiss()
            }
        }

        findViewById<Button>(R.id.ButtonCancel).setOnClickListener { dismiss() }



    }

}