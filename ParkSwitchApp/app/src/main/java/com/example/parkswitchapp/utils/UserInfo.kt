package com.example.parkswitchapp.utils
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.util.*

class UserInfo(
    val id: Int,
    val username: String,
    val tokens: Int,
    val location: String,
    val expirationTime: Date,
) {

    companion object {
        fun init_user_info(user: JSONObject) : UserInfo {

            val userId = user.get("id") as Int
            val username = user.get("username") as String
            val tokens = user.get("tokens") as Int
            val location = user.get("location") as String
            val expirationTime = Date(user.get("expirationTime") as Long)

            return UserInfo(userId, username,  tokens, location, expirationTime)
        }
    }
}
