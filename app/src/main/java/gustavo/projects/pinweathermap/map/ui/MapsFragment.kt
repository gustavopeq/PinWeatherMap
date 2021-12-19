package gustavo.projects.pinweathermap.map.ui

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import gustavo.projects.pinweathermap.R
import gustavo.projects.pinweathermap.domain.model.LocationWeather
import gustavo.projects.pinweathermap.map.viewmodel.MapViewModel

class MapsFragment : Fragment(),
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveStartedListener
{

    private lateinit var googleMap: GoogleMap

    private val viewModel: MapViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        viewModel.locationWeatherInfo.observe(viewLifecycleOwner) { locationWeather ->
            if(locationWeather != null) {
                addMarker(locationWeather)
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        googleMap.setOnMapClickListener(this)
        googleMap.setOnCameraIdleListener(this)
        googleMap.setOnCameraMoveStartedListener(this)
    }

    private fun addMarker(locationWeather: LocationWeather) {
        googleMap.addMarker(
                MarkerOptions()
                        .position(LatLng(locationWeather.latitude, locationWeather.longitude))
                        .title("${locationWeather.name} ${locationWeather.temp} °C"))!!.showInfoWindow()
    }

    override fun onMapClick(p0: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(p0))
    }

    override fun onCameraIdle() {
        googleMap.clear()
        viewModel.getWeatherByCoord(googleMap.cameraPosition.target.latitude,
                googleMap.cameraPosition.target.longitude)
    }

    override fun onCameraMoveStarted(p0: Int) {
        googleMap.clear()
    }
}