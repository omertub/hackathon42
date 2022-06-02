package com.example.parkswitchapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.NumberPicker


class TimePickerDialog(context: Context) : Dialog(context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.time_picker_dialog)
        findViewById<Button>(R.id.ButtonConfirm).setOnClickListener { }
        findViewById<Button>(R.id.ButtonCancel).setOnClickListener { dismiss() }

        val picker : NumberPicker = findViewById(R.id.PickerTime)
        picker.maxValue = 15
        picker.value = 5
        picker.wrapSelectorWheel = false


    }

}