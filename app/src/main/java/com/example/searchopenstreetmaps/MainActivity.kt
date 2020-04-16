package com.example.searchopenstreetmaps

import android.Manifest
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {

    private var autoCompleteTextView: AutoCompleteTextView? = null
    private var url_base = "https://photon.komoot.de/api/"
    private var locationManager : LocationManager? = null
    private var queue: RequestQueue? = null
    private var lat = ""
    private  var lon = ""
    private var arrayModelLocation: Array<ModelLocation>? = null
    private var arrayListModelLocation: ArrayList<ModelLocation>? = null
    private var arrayAdapter: CustomAdapter? = null
    private var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)
        button = findViewById(R.id.button)
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        queue = Volley.newRequestQueue(this)
        arrayModelLocation = Array(5, { ModelLocation() })

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            var location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            lat = location?.latitude.toString()
            lon = location?.longitude.toString()
        }

        arrayListModelLocation  = ArrayList<ModelLocation>(arrayModelLocation!!.size)
        arrayAdapter = CustomAdapter(this, R.layout.item_location, arrayListModelLocation!!)
        autoCompleteTextView!!.setAdapter(arrayAdapter)

        autoCompleteTextView!!.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (p0?.length!! >= 3){
                    getSuggestions(p0.toString())
                }
            }

        })

        button!!.setOnClickListener(View.OnClickListener {
            for( item in arrayModelLocation!!){
                Log.e("Array", item.toString())

            }
        })
    }

    private fun getSuggestions(search: String){

        var url = url_base + "?q=" + search + "&lat=" + lat + "&lon=" + lon + "&limit=5"

        Log.d("url", url)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,

            Response.Listener { response ->

                //Log.e("RespuestaBusq", response.toString())
                var jsonArray = response.getJSONArray("features")

                for (i in 0 until 5) {

                    try {
                        arrayModelLocation?.get(i)?.lat = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getDouble(0)
                    } catch (e: Exception){
                        arrayModelLocation?.get(i)?.lat = 0.0

                    }
                    try {
                        arrayModelLocation?.get(i)?.lon = jsonArray.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getDouble(1)
                    } catch (e: Exception){
                        arrayModelLocation?.get(i)?.lon = 0.0
                    }
                    try {
                        arrayModelLocation?.get(i)?.country = jsonArray.getJSONObject(i).getJSONObject("properties").getString("country")
                    } catch (e: Exception){
                        arrayModelLocation?.get(i)?.country = ""
                    }
                    try {
                        arrayModelLocation?.get(i)?.state = jsonArray.getJSONObject(i).getJSONObject("properties").getString("state")

                    } catch (e: Exception){
                        arrayModelLocation?.get(i)?.state = ""
                    }
                    try {
                        arrayModelLocation?.get(i)?.name = jsonArray.getJSONObject(i).getJSONObject("properties").getString("name")
                    } catch (e: Exception){
                        arrayModelLocation?.get(i)?.name = ""
                    }
                    try {
                        arrayModelLocation?.get(i)?.city = jsonArray.getJSONObject(i).getJSONObject("properties").getString("city")
                    } catch (e: Exception){
                        arrayModelLocation?.get(i)?.city = ""
                    }

                }
                arrayListModelLocation?.clear()
                for (modelLocation in arrayModelLocation!!) {
                    arrayListModelLocation?.add(modelLocation)
                }
                arrayAdapter?.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->
                Log.e("RespuestaBusq", "ERROR")
            }
        )

        queue?.add(jsonObjectRequest)
    }


}
