package gustavo.projects.pinweathermap.map.ui

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import gustavo.projects.pinweathermap.R
import gustavo.projects.pinweathermap.map.viewmodel.MapViewModel

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener{

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
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        googleMap.setOnMapClickListener(this)

        p0.addMarker(
                MarkerOptions()
                        .position(LatLng(0.0, 0.0))
                        .title("Marker")
        )
    }

    override fun onMapClick(p0: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(p0))

        viewModel.getWeatherByCoord(p0.latitude, p0.longitude)
    }
}