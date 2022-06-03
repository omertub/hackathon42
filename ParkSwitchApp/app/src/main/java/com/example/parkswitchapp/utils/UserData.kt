package com.example.parkswitchapp.utils

import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject

class UserData {

    companion object {

        var id: Int? = null
        var username: String? = null
        var tokens: Int? = null
        var location: String? = null
        var parkerId: Int? = null

        fun init_user_data(user: JSONObject) {
            id = user.get("id") as Int
            username = user.get("username") as String
            tokens = user.get("tokens") as Int
        }
    }
}