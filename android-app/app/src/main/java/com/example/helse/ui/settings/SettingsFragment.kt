package com.example.helse.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.example.helse.R
import com.example.helse.utilities.*
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.onboarding_dashboard_desc.*

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_settings)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        val toolbarTitle = activity?.findViewById<TextView>(R.id.toolbar_title)
        toolbar?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        toolbarTitle?.text = findNavController().currentDestination?.label
    }


//    private lateinit var locationClient: FusedLocationProviderClient
//    private lateinit var preferences: Preferences
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.onboarding_dashboard_desc, container, false)
//        view.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
//        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//        preferences = Injector.getAppPreferences(requireContext())
//
//        return view
//    private lateinit var locationClient: FusedLocationProviderClient
//    private lateinit var preferences: Preferences
//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//    }

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val view = inflater.inflate(R.layout.onboarding_dashboard_desc, container, false)
//        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//        preferences = AppPreferences(requireContext())
//
//        return view
//    }
//
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        finishBtn.setOnClickListener {
//            startActivity(Intent(activity, MainActivity::class.java))
//            requireActivity().finish()
//        }
//
//        locationSwitch.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                getDeviceLocation()
//            }
//        }
//    }

//    private fun getDeviceLocation() {
//        if (preferences.locationPermissionGranted()) {
//            try {
//                val location = locationClient.lastLocation
//                location.addOnSuccessListener {
//                    val currentLocation = location.result
//                    // For demonstration purposes
//                    val latitude = "Latitude: " + currentLocation?.latitude.toString()
//                    val longtitude = "Longtitude: " + currentLocation?.longitude.toString()
//                    lat.text = latitude
//                    longt.text = longtitude
//                }
//            } catch (e: SecurityException) {
//                println(e)
//            }
//        } else {
//            requestPermissions(
//                arrayOf(LOCATION_PERMISSION),
//                LOCATION_PERMISSION_CODE
//            )
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            LOCATION_PERMISSION_CODE -> {
//                if (grantResults.isNotEmpty() && grantResults[0] == PERMISSION_GRANTED) {
//                    getDeviceLocation()
//                } else {
//                    "Velg område manuelt!".toast(context)
//                }
//            }
//            // check push-notification permission
//        }
//    }
}
