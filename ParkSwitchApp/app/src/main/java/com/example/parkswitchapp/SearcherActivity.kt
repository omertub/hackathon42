package com.example.parkswitchapp

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.*
import com.example.parkswitchapp.utils.APIUtil
import com.example.parkswitchapp.utils.UserData
import org.json.JSONObject

class SearcherActivity : AppCompatActivity() {

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_searcher)

        findViewById<Button>(R.id.confirm_switch_button).setOnClickListener {
            APIUtil.postRequest("park", JSONObject()
                .put("id", UserData.id)) {
                runOnUiThread {
                    Toast.makeText(this, "approved!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        findViewById<Button>(R.id.search_map_button).setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
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

