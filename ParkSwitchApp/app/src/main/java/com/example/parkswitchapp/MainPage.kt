package com.example.parkswitchapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.PopupMenu
import android.view.View

class MainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main_page)
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