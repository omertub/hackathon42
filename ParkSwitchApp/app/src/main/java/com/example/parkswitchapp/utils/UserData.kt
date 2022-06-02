package com.example.parkswitchapp.utils

import org.json.JSONObject

class UserData(
    val id: Int,
    val username: String,
    val tokens: Int,
    val location: String?,
    val parkerId: Int?,
) {

    companion object {
        fun parseLocation(lon: Double, lat: Double): String {
            return "$lon/$lat"
        }

        fun init_user_data(user: JSONObject) : UserData {
            val userId = user.get("id") as Int
            val username = user.get("username") as String
            val tokens = user.get("tokens") as Int
            val location = user.get("location") as String
            val parkid = user.get("parkId") as Int
            return UserData(userId, username,  tokens, location, parkid)
        }


    }
}