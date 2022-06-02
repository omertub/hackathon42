package com.example.parkswitchapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class User (username: String, password: String) {
    val username = username
    val password = password
}
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        val loginButton: Button = findViewById(R.id.login_button_id)
        loginButton.setOnClickListener { loginClicked(loginButton) }
    }

    private fun loginClicked(view: View) {
        val username: EditText = findViewById(R.id.username_id)
        val password: EditText = findViewById(R.id.password_id)
        val username_string = username.text.toString()
        val password_string = password.text.toString()
        val user = User(username_string, password_string)
        if (username_string =="admin" && password_string == "1234") {
            Toast.makeText(this,"LOGIN SUCCESFULL", Toast.LENGTH_SHORT).show()
            //startActivity(Intent(this, MainPage::class.java))
            startActivity(Intent(this, MapsActivity::class.java))
        }
        else {
            Toast.makeText(this,"LOGIN FAILED", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MapsActivity::class.java))
        }
        // Hide the keyboard.
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

    }
}