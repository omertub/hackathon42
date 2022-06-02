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
        val username = findViewById<EditText>(R.id.username_id).text.toString()
        val password = findViewById<EditText>(R.id.password_id).text.toString()

        APIUtil.postRequest(
            "login", JSONObject()
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
                    Toast.makeText(this.applicationContext, "id: $id", Toast.LENGTH_LONG).show()

                    // Hide the keyboard.
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                    val mainPageActivity = Intent(this, MainPage::class.java)
                    mainPageActivity.putExtra("user_data", id)
                    startActivity(mainPageActivity)
                }
            }
        }


        //send user_name and password to backend and get back name and user_id

        // TODO: Login

//        if (username_string == "" && password_string == "") {
//            Toast.makeText(this, "LOGIN SUCCESFULL", Toast.LENGTH_SHORT).show()
//            //startActivity(Intent(this, MainPage::class.java))
//
//            // get user data
//            APIUtil.getRequest("user?userId=1") {
//                // the object 'it' is the JSONObject that contains the backend response
//                // this callback is running on a separate thread, so if we want
//                // to update the UI we need to run it on a UI thread
//                runOnUiThread {
//                    val status = it.get("status") as String
//                    if (status != "OK") {
//                        Toast.makeText(this, "Error! $status", Toast.LENGTH_LONG).show()
//                    } else {
//                        val user = it.get("user") as JSONObject
//                        val userId = user.get("id") as Int
//                        val username = user.get("username") as String
//                        Toast.makeText(
//                            this,
//                            "Welcome $username! (userId: $userId)",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    }
//
//                }
//                val main_page_activity = Intent(this, MainPage::class.java)
//                main_page_activity.putExtra("user_data", 1) // TODO: change to id
//                startActivity(main_page_activity)
//            }
//            else {
//                Toast.makeText(this, "LOGIN FAILED", Toast.LENGTH_SHORT).show()
//                val mp_activity =
//                    Intent(this, MainPage::class.java)//FIXME:remove once login implemented
//                startActivity(mp_activity)
//            }
//            // Hide the keyboard.
//            val inputMethodManager =
//                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
//
//        }

    }

    private fun signupClicked(view: View) {}
}

