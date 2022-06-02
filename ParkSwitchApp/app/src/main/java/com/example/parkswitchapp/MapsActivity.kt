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
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.tasks.OnSuccessListener
import java.lang.Exception
import java.lang.Thread.sleep
import java.util.jar.Manifest

class usersWithLocations (user_id: String, name: String, location: LatLng, time: Double) {
    val name = name
    val user_id = user_id
    val location = location
    val time = time
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

    private fun orderClicked(button: Button) {
        //SEND TO BACKEND ORDER REQUEST!!!
        //IF ORDER WAS SUCCESSFUL:
        Toast.makeText(this,"order request sent successfully", Toast.LENGTH_SHORT).show()
//        if(true) {
//            Toast.makeText(this,"order request sent successfully", Toast.LENGTH_SHORT).show()
        dialog.dismiss()
//        }
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

        val parked_users = ArrayList<usersWithLocations>()
        parked_users.add(usersWithLocations("1","avi1",LatLng(32.07607580161331, 34.85193881644974),14.0))
        parked_users.add(usersWithLocations("2","avi2",LatLng(32.077594, 34.853908),5.5))
        parked_users.add(usersWithLocations("3","avi3",LatLng(32.099492, 34.859029),4.5))
        parked_users.add(usersWithLocations("4","avi4",LatLng(32.100656, 34.860145),9.0))
        parked_users.add(usersWithLocations("5","avi5",LatLng(32.101219, 34.857977),3.0))

        val hashMap : HashMap<LatLng, usersWithLocations>
                = HashMap<LatLng, usersWithLocations> ()

        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID)
        // Add a marker on the map coordinates.
        for (parked_user in parked_users) {
            try {
                hashMap.put(parked_user.location, parked_user) //should be string and not LatLng?
            } catch (e:Exception) {Toast.makeText(this,"SHAKED1", Toast.LENGTH_SHORT).show()}
            val color : Float
            if (parked_user.time<5.0)
                color = BitmapDescriptorFactory.HUE_GREEN
            else if (parked_user.time<10.0)
                color = BitmapDescriptorFactory.HUE_ORANGE
            else
                color = BitmapDescriptorFactory.HUE_RED
            mMap.addMarker(
                MarkerOptions()
                    .position(parked_user.location)
                    .icon(BitmapDescriptorFactory.defaultMarker(color))
                    .title(parked_user.toString())
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
            var user: usersWithLocations? = null
            user = hashMap.get(marker.position)
            //add more info - distance from me? TTL?
            dialog.setContentView(R.layout.layout_custom_dialog)
            dialog.getWindow()!!.setBackgroundDrawableResource(R.drawable.bg_window)

            val parker_name :String = user!!.name
            val leaving_in : String = user!!.time.toString()
            val parkerTextView = dialog.findViewById<TextView>(R.id.txtDesc)
            val leaveInTextView = dialog.findViewById<TextView>(R.id.leave_time)
            parkerTextView.text = parkerTextView.text.toString().plus(parker_name)
            leaveInTextView.text = leaveInTextView.text.toString().plus(leaving_in).plus(" minutes")
            val btnClose: ImageView = dialog.findViewById(R.id.btn_close)
            val orderButton : Button = dialog.findViewById(R.id.btn_order)
            orderButton.setOnClickListener { orderClicked(orderButton) }

            btnClose.setOnClickListener(View.OnClickListener() {
                view-> dialog.dismiss()
            })
            dialog.show()

            false //ARIK: forcing google to also activate default listener which opens directions
        }



        //mMap.getUiSettings().setMapToolbarEnabled(true)
    }
}