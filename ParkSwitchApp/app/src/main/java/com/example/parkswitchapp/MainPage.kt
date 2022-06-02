package com.example.parkswitchapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuInflater
import android.widget.PopupMenu
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.parkswitchapp.utils.APIUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.util.*
import kotlin.concurrent.thread

class MainPage : AppCompatActivity() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main_page)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // TODO: get user_data from extra
        // set listeners
        findViewById<Button>(R.id.ButtonFindParking).setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.ButtonParked).setOnClickListener {
            // TODO:    Get current location and save to server
            val addr : Address? = getLocation()
            if (addr != null) {
                APIUtil.postRequest("postParking", JSONObject()
                    .put("id", "user_id")//TODO - change to user_id
                    .put("location", "location_parser")) { // TODO: add location parser
                    runOnUiThread {
                        it.get("staus")
                        Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
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
      popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.my_profile-> {

                }
                R.id.log_out-> {

                }
            }
            true
        }
        popup.show()
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
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation():Address? {
        var ret_addr :Address? = null
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addr : Address
                        addr = geocoder.getFromLocation(location.latitude, location.longitude, 1)[0]
                        Toast.makeText(this, addr.getAddressLine(0).toString(), Toast.LENGTH_LONG).show()
                        ret_addr = addr
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
        return ret_addr
    }
}