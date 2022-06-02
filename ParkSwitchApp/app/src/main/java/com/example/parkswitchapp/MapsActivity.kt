package com.example.parkswitchapp

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
//import com.google.android.gms.location
import com.google.android.gms.maps.model.MarkerOptions
import com.example.parkswitchapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        val list_of_locations = ArrayList<LatLng>()
        list_of_locations.add(LatLng(32.07607580161331, 34.85193881644974))
        list_of_locations.add(LatLng(32.077594, 34.853908))
        list_of_locations.add(LatLng(32.099492, 34.859029))
        list_of_locations.add(LatLng(32.100656, 34.860145))
        list_of_locations.add(LatLng(32.101219, 34.857977))


        //val address1 = LatLng(32.07607580161331, 34.85193881644974)
        // Set the map type to Hybrid.
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID)
        // Add a marker on the map coordinates.
        for (location in list_of_locations) {
            mMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title(location.toString())
            )
        }
        //change HUE_AZURE to whatever values between 0 and 360 to control marker's color
        //.icon(BitmapDescriptorFactory.fromResource(R.drawable.gray_icon))
        // Move the camera to the map coordinates and zoom in closer.
        val zoom = 20f

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(location.latitude, location.longitude)))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(zoom))
        // Display traffic.
        mMap.setTrafficEnabled(true)

        mMap.setOnMarkerClickListener { marker ->
            //TODO: open here a window from left/right/down with user info and a button to order
            // this parking spot
            //mMap.getUiSettings().setMapToolbarEnabled(true)
            if (!marker.isInfoWindowShown) {
                marker.showInfoWindow()

            }

            Toast.makeText(this,marker.position.toString(), Toast.LENGTH_SHORT).show()
//            if (marker.isInfoWindowShown) {
//                marker.hideInfoWindow()
//            } else {
//                marker.showInfoWindow()
//            }
            //true
            false //ARIK: forcing google to also activate default listener which opens directions
        }

        //mMap.getUiSettings().setMapToolbarEnabled(true)
    }
}