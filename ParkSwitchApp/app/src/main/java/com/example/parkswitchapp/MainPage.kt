package com.example.parkswitchapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.MenuInflater
import android.view.View
import android.widget.*
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.parkswitchapp.utils.APIUtil
import com.example.parkswitchapp.utils.LocationUtil
import com.example.parkswitchapp.utils.UserData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.util.*


class MainPage : AppCompatActivity() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main_page)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // set listeners
        findViewById<Button>(R.id.ButtonFindParking).setOnClickListener {
            val intent = Intent(this, SearcherActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.ButtonDiscounts).setOnClickListener {
            val intent = Intent(this, DiscountsActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.ButtonParked).setOnClickListener { view -> parkedClicked(view) }
        findViewById<Button>(R.id.ButtonLeaveParking).setOnClickListener {
            TimePickerDialog(this).show()
            findViewById<TextView>(R.id.statusView).text = UserData.status
        }

        if (UserData.location == null) {
            UserData.status = "Idle"
        } else {
            UserData.status = "Parking"
        }
        findViewById<TextView>(R.id.statusView).text = UserData.status
        findViewById<TextView>(R.id.tokensView).text = UserData.tokens.toString()
        findViewById<TextView>(R.id.nameView).text = "Welcome Back ${UserData.username}"

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
        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.my_profile-> {
                    //add here profile page reference
                    dialog= Dialog(this)
                    dialog.setContentView(R.layout.my_info_layout)
                    dialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.bg_window)
                    val MyName = dialog.findViewById<TextView>(R.id.my_name_text)
                    val myTokens = dialog.findViewById<TextView>(R.id.tokens_text)
                    MyName.text = MyName.text.toString().plus(UserData.username)
                    myTokens.text = myTokens.text.toString().plus(UserData.tokens)
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


    private fun parkedClicked(view : View) {
        Toast.makeText(this, "location saved", Toast.LENGTH_SHORT).show()
        // Get current location and save to server
        getLocation() {
            APIUtil.postRequest("saveParkingLocation", JSONObject()
                .put("id", UserData.id)
                .put("location", it)) {
                runOnUiThread {
                    it.get("status")
                }
            }
        }
        UserData.status = "Parking"
        findViewById<TextView>(R.id.statusView).text = UserData.status
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation(callback: (location: String) -> Any) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addr : Address
                        addr = geocoder.getFromLocation(location.latitude, location.longitude, 1)[0]
                        Toast.makeText(this, addr.getAddressLine(0).toString(), Toast.LENGTH_LONG).show()
                        callback(LocationUtil.parseLocation(addr.longitude, addr.latitude))
                    }
                }
            } else {
                Toast.makeText(this, "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

}