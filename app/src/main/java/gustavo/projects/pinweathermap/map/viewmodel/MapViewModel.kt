package gustavo.projects.pinweathermap.map.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gustavo.projects.pinweathermap.domain.model.LocationWeather
import gustavo.projects.pinweathermap.domain.repository.MapRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapRepository: MapRepository
) : ViewModel() {

    private var _locationWeatherInfo = MutableLiveData<LocationWeather>()
    val locationWeatherInfo: LiveData<LocationWeather>
        get() = _locationWeatherInfo

    private var _bookmarksList = MutableLiveData<List<LocationWeather>>()
    val bookmarksList: LiveData<List<LocationWeather>>
        get() = _bookmarksList

    fun getWeatherByCoord(lat: Double, lon: Double) {
        viewModelScope.launch {
            _locationWeatherInfo.postValue(mapRepository.getWeatherByCoord(lat, lon))
        }
    }

    fun addBookmark(locationWeather: LocationWeather) {
        viewModelScope.launch {
            mapRepository.addBookmarkToDatabase(locationWeather)
        }
    }

    fun removeBookmark(locationWeather: LocationWeather) {
        viewModelScope.launch {
            mapRepository.removeBookmarkFromDatabase(locationWeather)
        }
    }

    fun loadAllBookmarks() {
        viewModelScope.launch {
            val bookmarkEntityList = mapRepository.getAllBookmarksFromDatabase()

            if(!bookmarkEntityList.isNullOrEmpty()) {

                val locationWeatherList = mutableListOf<LocationWeather>()
                bookmarkEntityList.forEach { bookmarkEntity ->
                    var weatherLocation = mapRepository.getWeatherByCoord(
                        bookmarkEntity.latitude,
                        bookmarkEntity.longitude)

                    if(weatherLocation != null) {
                        locationWeatherList.add(weatherLocation)
                    }
                }

                _bookmarksList.postValue(locationWeatherList)
            }
        }
    }
}