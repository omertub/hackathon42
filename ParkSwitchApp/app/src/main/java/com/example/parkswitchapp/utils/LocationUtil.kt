package com.example.parkswitchapp.utils

import com.google.android.gms.maps.model.LatLng

class LocationUtil {
    companion object {
        fun parseLocation(lon: Double, lat: Double): String {
            return "$lon/$lat"
        }

        fun splitLocation(location: String): LatLng {
            val res = location.split('/')
            return LatLng(res[1].toDouble(), res[0].toDouble())
        }
    }
}