package com.example.parkswitchapp

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.parkswitchapp.utils.APIUtil
import com.example.parkswitchapp.utils.UserData
import org.json.JSONObject
import java.util.*


class TimePickerDialog(context: Activity) : Dialog(context){

    val appContext = context
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.time_picker_dialog)

        val picker : NumberPicker = findViewById(R.id.PickerTime)
        picker.maxValue = 15
        picker.value = 5
        picker.wrapSelectorWheel = false

        findViewById<Button>(R.id.ButtonConfirm).setOnClickListener {
            UserData.status = "Leaving"

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
                            // TODO: change to dialog
                            val user  = it.get("parkerUser") as JSONObject
                            val username = user.get("username") as String
                            //POPUP DIALOG:
                            dialog = Dialog(this.appContext)
                            //add more info - distance from me? TTL?
                            dialog.setContentView(R.layout.match_layout)
                            dialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.bg_window)

                            val parkerTextView = dialog.findViewById<TextView>(R.id.matched_name)
                            parkerTextView.text = parkerTextView.text.toString().plus(username)
                            val btnClose: ImageView = dialog.findViewById(R.id.btn_close3)

                            btnClose.setOnClickListener(View.OnClickListener() {
                                    view-> dialog.dismiss()
                            })
                            dialog.show()


                            //
                            //Toast.makeText(this.appContext, "Parking committed by $username", Toast.LENGTH_LONG).show()
                            APIUtil.clean("parkingCommitted")

                            UserData.status = "Idle"

                        }
                    }
                }

                // set ws listener for parkingCompleted event
                APIUtil.on("parkingCompleted") {
                    this.appContext.runOnUiThread {
                        if (UserData.id == it.get("id") as Int) {
                            // TODO: change to dialog
                            Toast.makeText(
                                this.appContext,
                                "Parking completed. 10 tokens added successfully!",
                                Toast.LENGTH_LONG
                            ).show()
                            APIUtil.clean("parkingCompleted")
                        }
                    }
                }

                dismiss()
            }
        }

        findViewById<Button>(R.id.ButtonCancel).setOnClickListener { dismiss() }



    }

}