package com.example.helse.ui.uv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.helse.R
import com.example.helse.data.api.UvResponse
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.UvRepositoryImpl
import com.example.helse.viewmodels.UvViewModel
import kotlinx.android.synthetic.main.fragment_uv.*

class UvFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_uv, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        infoButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.uv_to_information)
        }

        // defaultLocation == user location or defined location during setup
        val defaultLocation = requireActivity().intent.getParcelableExtra("LOCATION")
            ?: Location("Alnabru", "Oslo", 2.00, 2.12, "NO0057A")

        location.text =

            getString(R.string.location_text, defaultLocation.location, defaultLocation.superlocation)

        val uvViewModel = ViewModelProviders.of(this).get(UvViewModel::class.java)
            .apply {
                uvRepository = UvRepositoryImpl(
                    UvResponse(
                        defaultLocation,
                        this@UvFragment
                    )
                )
            }

        uvViewModel.getUvForecast().observe(this, Observer { uvForecast ->
        })
    }
}
