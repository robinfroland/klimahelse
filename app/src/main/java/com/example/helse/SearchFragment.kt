package com.example.helse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helse.data.api.LocationResponse
import com.example.helse.adapters.LocationAdapter
import com.example.helse.data.database.LocalDatabase
import com.example.helse.data.entities.Location
import com.example.helse.data.repositories.LocationRepository
import com.example.helse.viewmodels.SearchViewModel
import com.example.helse.databinding.FragmentSearchBinding
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var viewAdapter: LocationAdapter
//    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()


        viewModel.getLocations().observe(this, Observer { locations ->
            recyclerView_locations.layoutManager = LinearLayoutManager(context)
            viewAdapter = LocationAdapter { location : Location -> locationClicked(location)}
            recyclerView_locations.adapter = viewAdapter
            viewAdapter.submitList(locations)
        })

//        val viewManager = LinearLayoutManager(context)
//        val viewAdapter = LocationAdapter(viewModel.)
//        recyclerView = recyclerView_locations.apply {
//            layoutManager = viewManager
//            adapter = viewAdapter
//        }
//        viewAdapter.submitData()
//        initRecyclerView()

//
        setHasOptionsMenu(true)
    }

    private fun locationClicked(location: Location) {
        val intent = Intent(activity, AirqualityActivity::class.java)
        val extras = Bundle()
        extras.putString("LOCATION", location.location)
        extras.putString("SUPERLOCATION", location.superlocation)
        intent.putExtras(extras)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchIcon = menu.findItem(R.id.search_icon)
        val searchView = searchIcon.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(userInput: String?): Boolean {
                viewAdapter.filter.filter(userInput)
                return false
            }

        })

    }

    private fun initRecyclerView() {

    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
            .apply {
                locationRepository = LocationRepository(
                    LocalDatabase.getInstance(requireContext()).locationDao(),
                    LocationResponse()
                )
            }

    }

//    private fun submitData(data: MutableList<Location>) {
//        viewModel.getLocations().observe(viewLifecycleOwner, Observer { locations ->
//            submitList(locations)
//        })
//    }

}
