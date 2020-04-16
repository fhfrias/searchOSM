package com.example.searchopenstreetmaps

class ModelLocation {

    var lat: Double? = null
    var lon: Double? = null
    var name: String? = null
    var country: String? = null
    var state: String? = null
    var city: String? = null

    override fun toString(): String {
        return "ModelLocation(lat=$lat, lon=$lon, name=$name, country=$country, state=$state, city=$city)"
    }


}