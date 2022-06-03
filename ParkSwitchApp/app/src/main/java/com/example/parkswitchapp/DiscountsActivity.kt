package com.example.parkswitchapp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView

class DiscountsActivity : AppCompatActivity() {

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discounts)
    }

    fun showPopup(v : View){
        val user_name = "avi" //NEED TO GET NAME BACK FROM SERVER AFTER LOGIN AND PASS IT HERE WITH PUTEXRA
        val tokens = "50" //SAME
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.options, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.my_profile-> {
                    //add here profile page reference
                    dialog= Dialog(this)
                    dialog.setContentView(R.layout.my_info_layout)
                    dialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.bg_window)
                    val MyName = dialog.findViewById<TextView>(R.id.my_name_text)
                    val myTokens = dialog.findViewById<TextView>(R.id.tokens_text)
                    MyName.text = MyName.text.toString().plus(user_name)
                    myTokens.text = myTokens.text.toString().plus(tokens)

                    val btnClose: ImageView = dialog.findViewById(R.id.btn_close2)
                    btnClose.setOnClickListener(View.OnClickListener() {
                            view-> dialog.dismiss()
                    })
                    dialog.show()

                }
                R.id.log_out-> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }
        popup.show()
    }
}