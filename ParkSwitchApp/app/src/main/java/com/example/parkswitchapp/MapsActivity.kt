package com.example.parkswitchapp

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.MarkerOptions
import com.example.parkswitchapp.databinding.ActivityMapsBinding
import com.example.parkswitchapp.utils.APIUtil
import com.example.parkswitchapp.utils.LocationUtil
import com.example.parkswitchapp.utils.UserData
import com.example.parkswitchapp.utils.UserInfo
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.tasks.OnSuccessListener
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.lang.Thread.sleep
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.thread

class UsersWithLocations (userInfo: UserInfo) {
    val username = userInfo.username
    val id = userInfo.id
    val location = LocationUtil.splitLocation(userInfo.location!!)
    val time = (userInfo.expirationTime.time - Date().time) / 1000 / 60
}

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    private lateinit var orderButton: Button
    private lateinit var dialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        fetchLocation()

        //val orderButton: Button = findViewById(R.id.btn_order)
    }

    private fun fetchLocation() {
        fusedLocationProviderClient!!.getLastLocation()
            .addOnSuccessListener(this, OnSuccessListener<Location>() {
                location ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        val latLng = LatLng(location.latitude, location.longitude)
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
                    }
            })
    }

    private fun orderClicked(button: Button, user: UsersWithLocations) {
        APIUtil.postRequest("commitParking", JSONObject()
            .put("id", UserData.id)
            .put("ownerId", user.id)) {
            runOnUiThread {
                val status = it.get("status") as String
                if (status == "OK") {
                    Toast.makeText(this,"order request sent successfully", Toast.LENGTH_SHORT).show()
                }
            }
        }

        dialog.dismiss()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true

        // Need to add here structs that contain user info and locations and times
        APIUtil.getRequest("markers") {
            runOnUiThread {
                val parked_users = ArrayList<UsersWithLocations>()

                val markers = it.get("markers") as JSONArray
                for (i in 0 until markers.length()) {
                    val marker = markers[i] as JSONObject
                    val userInfo = UserInfo.init_user_info(marker)
                    parked_users.add(UsersWithLocations(userInfo))
                }
                //ADD FALSE PEOPLE:
                parked_users.add(UsersWithLocations(UserInfo(1111111111,"Avi Cohen",0,"34.85193881644974/32.07607580161331",Date(Date().time+3*60*1000))))
                parked_users.add(UsersWithLocations(UserInfo(2111111111,"Omer Tuburg",0,"34.853908/32.077594",Date(Date().time+6*60*1000))))
                parked_users.add(UsersWithLocations(UserInfo(41111111,"Matan Eshel",0,"34.859029/32.099492",Date(Date().time+12*60*1000))))
                parked_users.add(UsersWithLocations(UserInfo(51111111,"Arik Herzog",0,"34.860145/32.100656",Date(Date().time+4*60*1000))))
                parked_users.add(UsersWithLocations(UserInfo(61111111,"Yehonatan Mizrahi",0,"34.857977/32.101219",Date(Date().time+15*60*1000))))
                parked_users.add(UsersWithLocations(UserInfo(71111111,"Eli",0,"34.857977/32.102219",Date(Date().time+8*60*1000))))
                parked_users.add(UsersWithLocations(UserInfo(81111111,"Yoni Shitrit",0,"34.856977/32.101219",Date(Date().time+20*60*1000))))

                //parked_users.add(UsersWithLocations("8","Avia Cohen",LatLng(32.101519, 34.858977),3.0))
                //parked_users.add(UsersWithLocations("9","Ruth Nimni",LatLng(32.102219, 34.847977),12.0))
//                parked_users.add(UsersWithLocations("10","Oriya Cohen",LatLng(32.101219, 34.877977),7.0))
//                parked_users.add(UsersWithLocations("11","Mazal Levi",LatLng(32.101239, 34.837977),6.0))
//                parked_users.add(UsersWithLocations("12","Kobi Mahat",LatLng(32.101229, 34.887977),3.0))
                //

                val hashMap : HashMap<LatLng, UsersWithLocations>
                        = HashMap<LatLng, UsersWithLocations> ()

                //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID)
                // Add a marker on the map coordinates.
                for (parked_user in parked_users) {
                    try {
                        hashMap.put(parked_user.location, parked_user) //should be string and not LatLng?
                    } catch (e:Exception) {Toast.makeText(this,"SHAKED1", Toast.LENGTH_SHORT).show()}
                    val color : Float
                    if (parked_user.time<5.0)
                        color = BitmapDescriptorFactory.HUE_RED
                    else if (parked_user.time<10.0)
                        color = BitmapDescriptorFactory.HUE_ORANGE
                    else
                        color = BitmapDescriptorFactory.HUE_GREEN
                    mMap.addMarker(
                        MarkerOptions()
                            .position(parked_user.location)
                            .icon(BitmapDescriptorFactory.defaultMarker(color))
                    )
                }
                //change HUE_AZURE to whatever values between 0 and 360 to control marker's color
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.gray_icon))
                // Move the camera to the map coordinates and zoom in closer.

                //mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
                //mMap.moveCamera(CameraUpdateFactory.zoomTo(20f))
                // Display traffic.
                mMap.setTrafficEnabled(true)

                mMap.setOnMarkerClickListener { marker ->
                    //val dialog: Dialog = Dialog(this)
                    dialog= Dialog(this)
                    var user: UsersWithLocations? = null
                    user = hashMap.get(marker.position)
                    //add more info - distance from me? TTL?
                    dialog.setContentView(R.layout.layout_custom_dialog)
                    dialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.bg_window)

                    val parker_name :String = user!!.username
                    val leaving_in : String = user!!.time.toString()
                    val parkerTextView = dialog.findViewById<TextView>(R.id.txtDesc)
                    val leaveInTextView = dialog.findViewById<TextView>(R.id.leave_time)
                    parkerTextView.text = parkerTextView.text.toString().plus(parker_name)
                    leaveInTextView.text = leaveInTextView.text.toString().plus(leaving_in).plus(" minutes")
                    val btnClose: ImageView = dialog.findViewById(R.id.btn_close)
                    val orderButton : Button = dialog.findViewById(R.id.btn_order)
                    orderButton.setOnClickListener { orderClicked(orderButton, user) }

                    btnClose.setOnClickListener(View.OnClickListener() {
                            view-> dialog.dismiss()
                    })
                    dialog.show()

                    false //ARIK: forcing google to also activate default listener which opens directions
                }
            }


        }

    }
}