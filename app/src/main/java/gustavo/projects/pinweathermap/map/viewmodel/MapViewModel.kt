package gustavo.projects.pinweathermap.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gustavo.projects.pinweathermap.domain.model.LocationWeather
import gustavo.projects.pinweathermap.domain.repository.MapRepository
import kotlinx.coroutines.launch

class MapViewModel: ViewModel() {

    private var mapRepository: MapRepository = MapRepository()

    private var _locationWeatherInfo = MutableLiveData<LocationWeather>()
    val locationWeatherInfo: LiveData<LocationWeather>
        get() = _locationWeatherInfo

    fun getWeatherByCoord(lat: Double, lon: Double) {
        viewModelScope.launch {

            _locationWeatherInfo.postValue(mapRepository.getWeatherByCoord(lat, lon))
        }
    }
}