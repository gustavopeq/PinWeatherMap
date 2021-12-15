package gustavo.projects.pinweathermap.map.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gustavo.projects.pinweathermap.domain.repository.MapRepository
import gustavo.projects.pinweathermap.network.NetworkLayer
import kotlinx.coroutines.launch

class MapViewModel: ViewModel() {

    private var mapRepository: MapRepository = MapRepository()

    fun getWeatherByCoord(lat: Double, lon: Double) {
        viewModelScope.launch {

            mapRepository.getWeatherByCoord(lat, lon)
        }
    }
}