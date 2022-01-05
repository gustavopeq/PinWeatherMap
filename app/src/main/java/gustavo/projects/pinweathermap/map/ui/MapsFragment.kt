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

    private var isExploring: Boolean = true

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

        viewModel.bookmarksList.observe(viewLifecycleOwner) { locationWeatherList ->
            if(!locationWeatherList.isNullOrEmpty()) {
                locationWeatherList.forEach { locationWeather ->
                    addMarker(locationWeather)
                }
            }
        }

        bottomSheetBinding = mapBinding.bottomSheet
        bottomSheet = bottomSheetBinding.root
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottomSheetBehavior.addBottomSheetCallback(object:
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED ->
                        mapBinding.bookmarkExplorerBtn.visibility = View.GONE

                    BottomSheetBehavior.STATE_COLLAPSED ->
                        mapBinding.bookmarkExplorerBtn.visibility = View.VISIBLE

                    BottomSheetBehavior.STATE_HIDDEN ->
                        mapBinding.bookmarkExplorerBtn.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Nothing to do
            }

        })

        mapBinding.bookmarkExplorerBtn.setOnClickListener {
            if(isExploring) {
                Log.d("print", "OPA")
                onShowBookmarks()
            }else {
                onExploreMode()
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        googleMap.setOnMapClickListener(this)
        googleMap.setOnCameraIdleListener(this)
        googleMap.setOnCameraMoveStartedListener(this)
        googleMap.setOnInfoWindowClickListener {
            if(it.tag is LocationWeather) {
                onBottomSheetExpand(it.tag as LocationWeather)
                mapBinding.bookmarkExplorerBtn.visibility = View.GONE
            }
        }
    }

    private fun addMarker(locationWeather: LocationWeather) {
        if(!locationWeather.name.isNullOrBlank()) {
            val markerOptions = MarkerOptions()
                .position(LatLng(locationWeather.latitude, locationWeather.longitude))
                .title("${locationWeather.name}")

            googleMap.addMarker(markerOptions)!!.apply {
                tag = locationWeather
                snippet = "${locationWeather.temp}°C"
                showInfoWindow()
            }
        }
    }

    override fun onMapClick(p0: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(p0))
    }

    override fun onCameraIdle() {
        if(isExploring) {
            googleMap.clear()
            viewModel.getWeatherByCoord(
                googleMap.cameraPosition.target.latitude,
                googleMap.cameraPosition.target.longitude
            )
        }
    }

    override fun onCameraMoveStarted(p0: Int) {
        if(isExploring) {
            googleMap.clear()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun onBottomSheetExpand(locationWeather: LocationWeather) {
        bottomSheetBinding.cityName.text = locationWeather.name
        bottomSheetBinding.temperature.text = "${locationWeather.temp}°C"
        bottomSheetBinding.feelsLikeTextView.text = "${locationWeather.feelsLike}°C"

        setWeatherLottieAnim(locationWeather)

        bottomSheetBinding.maxMinTextView.text = "${locationWeather.tempMax}°C / ${locationWeather.tempMin}°C"
        bottomSheetBinding.cloudsTextView.text = "${locationWeather.clouds}%"
        bottomSheetBinding.humidityTextView.text = "${locationWeather.humidity}%"
        bottomSheetBinding.pressureTextView.text = "${locationWeather.pressure} hPa"
        bottomSheetBinding.windTextView.text = "${locationWeather.windSpeed} m/s"

        if(isExploring) {
            bottomSheetBinding.addBookmarkBtn.visibility = View.VISIBLE
            bottomSheetBinding.removeBookmarkBtn.visibility = View.GONE
        }else {
            bottomSheetBinding.removeBookmarkBtn.visibility = View.VISIBLE
            bottomSheetBinding.addBookmarkBtn.visibility = View.GONE
        }

        bottomSheetBinding.addBookmarkBtn.setOnClickListener {
            viewModel.addBookmark(locationWeather)
            bottomSheetBinding.removeBookmarkBtn.visibility = View.VISIBLE
            bottomSheetBinding.addBookmarkBtn.visibility = View.GONE
        }

        bottomSheetBinding.removeBookmarkBtn.setOnClickListener {
            viewModel.removeBookmark(locationWeather)
            bottomSheetBinding.addBookmarkBtn.visibility = View.VISIBLE
            bottomSheetBinding.removeBookmarkBtn.visibility = View.GONE
            if(!isExploring) {
                onShowBookmarks()
            }
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setWeatherLottieAnim(locationWeather: LocationWeather) {
        var isDay: Boolean

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
    }

    private fun onExploreMode() {
        googleMap.clear()
        mapBinding.mapTargetImageView.visibility = View.VISIBLE
        mapBinding.bookmarkExplorerBtn.text = "Show Bookmarks"
        isExploring = true
    }

    private fun onShowBookmarks() {
        googleMap.clear()
        mapBinding.mapTargetImageView.visibility = View.GONE
        mapBinding.bookmarkExplorerBtn.text = "Explore Mode"
        isExploring = false

        viewModel.loadAllBookmarks()
    }
}