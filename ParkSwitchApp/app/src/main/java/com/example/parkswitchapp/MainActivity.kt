package com.example.parkswitchapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.parkswitchapp.utils.APIUtil
import com.example.parkswitchapp.utils.UserData
import org.json.JSONObject

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
        val username = findViewById<EditText>(R.id.username_id).text.toString()
        val password = findViewById<EditText>(R.id.password_id).text.toString()

        APIUtil.postRequest(
            "login", JSONObject()
                .put("username", username)
                .put("password", password) )
        {
            runOnUiThread {
                val status = it.get("status") as String
                if (status != "OK") {
                    Toast.makeText(this.applicationContext, "Error! $status", Toast.LENGTH_LONG).show()
                } else {
                    val user = it.get("user") as JSONObject
                    UserData.init_user_data(user)
                    Toast.makeText(this.applicationContext, "id: ${UserData.id}", Toast.LENGTH_LONG).show()

                    // Hide the keyboard.
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

                    // Call MainPage activity
                    val intent = Intent(this, MainPage::class.java)
                    startActivity(intent)
                }
            }
        }

    }

    private fun signupClicked(view: View) {
        val username = findViewById<EditText>(R.id.username_id).text.toString()
        val password = findViewById<EditText>(R.id.password_id).text.toString()

        APIUtil.postRequest(
            "signup", JSONObject()
                .put("username", username)
                .put("password", password) )
        {
            runOnUiThread {
                it.get("status")
                val status = it.get("status") as String
                if (status != "OK") {
                    Toast.makeText(this.applicationContext, "Error! $status", Toast.LENGTH_LONG).show()
                } else {
                    val user = it.get("user") as JSONObject
                    var id = user.get("id") as Int
                    Toast.makeText(this.applicationContext, "Signed up! id: $id", Toast.LENGTH_LONG).show()

                    // Hide the keyboard.
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

                }
            }
        }
    }
}

