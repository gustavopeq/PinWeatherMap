package gustavo.projects.pinweathermap.map.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gustavo.projects.pinweathermap.domain.repository.MapRepository
import kotlinx.coroutines.launch

class MapViewModel: ViewModel() {

    private var mapRepository: MapRepository = MapRepository()

    fun getWeatherByCoord(lat: Double, lon: Double) {
        viewModelScope.launch {

            mapRepository.getWeatherByCoord(lat, lon)
        }
    }
}