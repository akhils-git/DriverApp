package com.girfalco.driverapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.girfalco.driverapp.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize dashboard components
        setupDashboard()
    }
    
    private fun setupDashboard() {
        // Set up route status
        binding.routeStatusText.text = "Ready to Start"
        binding.binsCollectedCount.text = "0"
        binding.binsRemainingCount.text = "24"
        
        // Set up button listeners
        binding.startRouteButton.setOnClickListener {
            startRoute()
        }
        
        binding.viewMapButton.setOnClickListener {
            openMapView()
        }
        
        binding.viewRoutesButton?.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_routesFragment)
        }
    }
    
    private fun startRoute() {
        binding.routeStatusText.text = "Route Active"
        binding.startRouteButton.text = "Complete Route"
        // TODO: Implement route start logic
    }
    
    private fun openMapView() {
        // TODO: Implement map navigation
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}