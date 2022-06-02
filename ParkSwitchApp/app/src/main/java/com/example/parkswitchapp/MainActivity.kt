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
        findViewById<Button>(R.id.ButtonLogin).setOnClickListener { view -> loginClicked(view) }
        findViewById<Button>(R.id.ButtonSignup).setOnClickListener { view -> signupClicked(view) }
        findViewById<Button>(R.id.ButtonSkip).setOnClickListener { view -> startActivity(Intent(this, MainPage::class.java)) }

    }

    private fun loginClicked(view: View) {
        val username: EditText = findViewById(R.id.username_id)
        val password: EditText = findViewById(R.id.password_id)
        val username_string = username.text.toString()
        val password_string = password.text.toString()
        val user = User(username_string, password_string)

        //Intent i = new Intent(CurrentActivity.this, NewActivity.class);
        //i.putExtra("key",value);
        //startActivity(i);

        //send user_name and password to backend and get back name and user_id

        if (username_string =="" && password_string == "") {
            Toast.makeText(this,"LOGIN SUCCESFULL", Toast.LENGTH_SHORT).show()
            //startActivity(Intent(this, MainPage::class.java))
            val main_page_activity = Intent(this, MainPage::class.java)
            main_page_activity.putExtra("user_id","1111")
            main_page_activity.putExtra("name","Omer")
            startActivity(main_page_activity)
        }
        else {
            Toast.makeText(this,"LOGIN FAILED", Toast.LENGTH_SHORT).show()
            val mp_activity = Intent(this, MainPage::class.java)//FIXME:remove once login implemented
            startActivity(mp_activity)
        }
        // Hide the keyboard.
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

    }

    private fun signupClicked(view: View) { }
}

