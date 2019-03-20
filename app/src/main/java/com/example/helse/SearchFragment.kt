package com.example.helse

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helse.adapters.LocationAdapter
import com.example.helse.data.api.LocationResponse
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.LocationRepository
import com.example.helse.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var viewAdapter: LocationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        submitData()

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchIcon = menu.findItem(R.id.search_icon)
        val searchView = searchIcon.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(userInput: String): Boolean {
                if (userInput.isNotEmpty()) {
                    viewAdapter.filter.filter(userInput)
                    search_view.visibility = View.VISIBLE
                } else {
                    search_view.visibility = View.GONE
                }
                return false
            }
        })
    }

    private fun submitData() {
        viewModel.getLocations().observe(this, Observer { locations ->
            recyclerView_locations.layoutManager = LinearLayoutManager(context)
            viewAdapter = LocationAdapter { location: Location -> locationClicked(location) }
            recyclerView_locations.adapter = viewAdapter
            viewAdapter.submitList(locations)
        })
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
            .apply {
                locationRepository = LocationRepository(
                    LocalDatabase.getInstance(requireContext()).locationDao(),
                    LocationResponse(this@SearchFragment.activity)
                )
            }
    }

    private fun locationClicked(location: Location) {
        val intent = Intent(activity, AirqualityFragment::class.java)
        intent.putExtra("LOCATION", location)
        startActivity(intent)
    }

}
