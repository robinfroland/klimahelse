package com.example.helse


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.helse.utilities.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_preferences.*


class PreferencesFragment : Fragment() {
    // extend PreferenceFragment later

    private lateinit var locationClient: FusedLocationProviderClient
    private lateinit var preferences: Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_preferences, container, false)
        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        preferences = AppPreferences(requireContext())

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finishBtn.setOnClickListener {
            startActivity(Intent(activity, MainActivity::class.java))
            requireActivity().finish()
        }

        locationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                getDeviceLocation()
            }
        }
    }

    private fun getDeviceLocation() {
        if (preferences.locationPermissionGranted()) {
            try {
                val location = locationClient.lastLocation
                location.addOnSuccessListener {
                    val currentLocation = location.result
                    // For demonstration purposes
                    val latitude = "Latitude: " + currentLocation?.latitude.toString()
                    val longtitude = "Longtitude: " + currentLocation?.longitude.toString()
                    lat.text = latitude
                    longt.text = longtitude
                }
            } catch (e: SecurityException) {
                println(e)
            }
        } else {
            requestPermissions(
                arrayOf(LOCATION_PERMISSION),
                LOCATION_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
                    getDeviceLocation()
                } else {
                    Toast.makeText(context, "Velg område manuelt!", Toast.LENGTH_LONG).show()
                }
            }
            // check push-notification permission
        }
    }

    companion object {
        fun newInstance() = PreferencesFragment()
    }

}
