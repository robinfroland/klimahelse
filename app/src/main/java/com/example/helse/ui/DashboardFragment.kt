package com.example.helse.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helse.R
import com.example.helse.adapters.ModuleAdapter
import com.example.helse.data.entities.ModuleCard
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private lateinit var enabledModules: ArrayList<ModuleCard>
    private lateinit var viewAdapter: ModuleAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        submitModules()
        super.onViewCreated(view, savedInstanceState)
        val viewManager = LinearLayoutManager(context)
        viewAdapter = ModuleAdapter(enabledModules)
        module_list.apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun submitModules() {
        enabledModules = arrayListOf(
            ModuleCard(R.drawable.ic_launcher_foreground, "luftkvalitet", "HIGH", true),
            ModuleCard(R.drawable.ic_launcher_foreground, "UV-Stråling", "LOW", true),
            ModuleCard(R.drawable.ic_launcher_foreground, "Luftfuktighet", "MEDIUM", true)
        )
    }
}



