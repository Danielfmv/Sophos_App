package com.example.sophos.UI.Offices

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentProviderClient
import android.content.Context
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.sophos.Data.Model.OfficesItems
import com.example.sophos.R
import com.example.sophos.ViewModels.LoginViewModel
import com.example.sophos.ViewModels.OfficesViewModel
import com.example.sophos.databinding.FragmentLoginBinding
import com.example.sophos.databinding.FragmentOfficesBinding
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.internal.artificialFrame
import java.util.jar.Manifest
import com.google.android.gms.location.LocationServices

class OfficesFragment : Fragment() {

    private val officesViewModel : OfficesViewModel by viewModels()

    private lateinit var mMap : GoogleMap

    private var citiesObserved : MutableList<OfficesItems> = mutableListOf()

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

    private var _binding : FragmentOfficesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOfficesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Crea la lista de la variable "citiesObserved" los datos obtenidos de la API
        officesViewModel.officesResponseLiveData.observe(viewLifecycleOwner) {
            officesViewModel.getOffices()
            for (cities in officesViewModel.officesResponseLiveData.value!!) {
                citiesObserved.add(cities)

                // Agrega el signo " - " a la Latitud de la oficina de Chile
                if (cities.officeCity == "Chile" && !cities.officeLatit.startsWith("-")) {
                    cities.officeLatit = "-" + cities.officeLatit
                }
            }

            createMarker()

        }

        val mapfragment = childFragmentManager.findFragmentById(R.id.offices_map) as SupportMapFragment
        mapfragment.getMapAsync { googleMap ->
            mMap = googleMap
            createMarker()
            enableLocation()
        }

    }

    private fun createMarker () {
        for (cities in citiesObserved) {
            val marker = MarkerOptions()
                .position(LatLng(cities.officeLatit.toDouble(), cities.officeLongit.toDouble()))
                .title(cities.officeName)
            mMap.addMarker(marker)
        }
    }

    private fun isLocationPermissionGranted () = ContextCompat.checkSelfPermission(
        requireActivity(),
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation () {
        if (!::mMap.isInitialized) return
        if (isLocationPermissionGranted()) {
            mMap.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission () {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(requireContext(), "Ve a ajustes y acepta los permisos de ubicación", Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
            } else {
                Toast.makeText(requireContext(), "Para activar la localización debes aceptar los permisos", Toast.LENGTH_SHORT).show()
            } else -> {}
        }
    }

//    override fun onMyLocationButtonClick(): Boolean {
//        return false
//    }
}

