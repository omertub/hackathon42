package com.example.parkswitchapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.PopupMenu
import android.view.View
import android.widget.Button

class MainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main_page)

        // set listeners
        findViewById<Button>(R.id.ButtonFindParking).setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.ButtonParked).setOnClickListener {
            // TODO:    Get current location and save to server
        }
        findViewById<Button>(R.id.ButtonLeaveParking).setOnClickListener {
//            val intent = Intent(this, MapsActivity::class.java)
//            startActivity(intent)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val inflater : MenuInflater = getMenuInflater()
//        inflater.inflate(R.menu.options, menu);
//        return true
//    }
    fun showPopup(v : View){
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.options, popup.menu)
//        popup.setOnMenuItemClickListener { menuItem ->
//            when(menuItem.itemId){
//                R.id.action1-> {
//
//                }
//                R.id.action2-> {
//
//                }
//            }
//            true
//        }
        popup.show()
    }
}