package gustavo.projects.pinweathermap.map.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import gustavo.projects.pinweathermap.Constants
import gustavo.projects.pinweathermap.R
import gustavo.projects.pinweathermap.databinding.BottomSheetDetailsBinding
import gustavo.projects.pinweathermap.databinding.FragmentMapsBinding
import gustavo.projects.pinweathermap.domain.model.LocationWeather
import gustavo.projects.pinweathermap.map.viewmodel.MapViewModel

@AndroidEntryPoint
class MapsFragment : Fragment(),
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveStartedListener
{

    private lateinit var googleMap: GoogleMap

    private val viewModel: MapViewModel by viewModels()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var bottomSheet: View

    private lateinit var mapBinding: FragmentMapsBinding
    private lateinit var bottomSheetBinding: BottomSheetDetailsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mapBinding = FragmentMapsBinding.inflate(inflater, container, false)

        return mapBinding.root
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


        bottomSheetBinding = mapBinding.bottomSheet
        bottomSheet = bottomSheetBinding.root
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        googleMap.setOnMapClickListener(this)
        googleMap.setOnCameraIdleListener(this)
        googleMap.setOnCameraMoveStartedListener(this)
        googleMap.setOnInfoWindowClickListener {
            if(it.tag is LocationWeather) {
                onBottomSheetExpand(it.tag as LocationWeather)
            }
        }
    }

    private fun addMarker(locationWeather: LocationWeather) {
        if(!locationWeather.name.isNullOrBlank()) {
            val markerOptions = MarkerOptions()
                .position(LatLng(locationWeather.latitude, locationWeather.longitude))
                .title("${locationWeather.name} ${locationWeather.temp} °C")

            googleMap.addMarker(markerOptions)!!.apply {
                tag = locationWeather
                showInfoWindow()
            }
        }
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
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun onBottomSheetExpand(locationWeather: LocationWeather) {
        bottomSheetBinding.cityName.text = locationWeather.name
        bottomSheetBinding.temperature.text = locationWeather.temp.toString()

        var isDay = true

        if(locationWeather.unixTime > locationWeather.sunRiseTime) {
            isDay = locationWeather.unixTime < locationWeather.sunSetTime
        }else{
            isDay = locationWeather.unixTime > locationWeather.sunRiseTime
        }

        if(isDay){
            if (locationWeather.description.contains("snow")) {
                bottomSheetBinding.weatherAnim.setAnimation(R.raw.day_snowy_anim)
            } else if (locationWeather.description.contains("rain")) {
                bottomSheetBinding.weatherAnim.setAnimation(R.raw.day_rainy_anim)
            } else if (locationWeather.description.contains("scattered clouds")
                || locationWeather.description.contains("broken clouds")
                || locationWeather.description.contains("few clouds")) {
                bottomSheetBinding.weatherAnim.setAnimation(R.raw.day_cloudy_anim)
            }else if (locationWeather.description.contains("clouds")) {
                bottomSheetBinding.weatherAnim.setAnimation(R.raw.cloudy_anim)
            }else{
                bottomSheetBinding.weatherAnim.setAnimation(R.raw.day_clear_anim)
            }
        }else{
            if (locationWeather.description.contains("snow")) {
                bottomSheetBinding.weatherAnim.setAnimation(R.raw.night_snowy_anim)
            }else if (locationWeather.description.contains("heavy rain")) {
                bottomSheetBinding.weatherAnim.setAnimation(R.raw.storm_anim)
            } else if (locationWeather.description.contains("rain")) {
                bottomSheetBinding.weatherAnim.setAnimation(R.raw.night_rainy_anim)
            } else if (locationWeather.description.contains("scattered clouds")
                || locationWeather.description.contains("broken clouds")
                || locationWeather.description.contains("few clouds")) {
                bottomSheetBinding.weatherAnim.setAnimation(R.raw.night_cloudy_anim)
            }else if (locationWeather.description.contains("clouds")) {
                bottomSheetBinding.weatherAnim.setAnimation(R.raw.cloudy_anim)
            }else{
                bottomSheetBinding.weatherAnim.setAnimation(R.raw.night_clear_anim)
            }
        }

        bottomSheetBinding.weatherAnim.playAnimation()

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}