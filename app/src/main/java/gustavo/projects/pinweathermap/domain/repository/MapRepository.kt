package gustavo.projects.pinweathermap.domain.repository

import android.util.Log
import gustavo.projects.pinweathermap.domain.mapper.LocationWeatherMapper
import gustavo.projects.pinweathermap.domain.model.LocationWeather
import gustavo.projects.pinweathermap.network.NetworkLayer

class MapRepository {

    suspend fun getWeatherByCoord(lat: Double, lon: Double): LocationWeather? {

        val request = NetworkLayer.apiClient.getWeatherByCoord(lat,lon)

        if(request.failed || !request.isSuccessful) {
            return null
        }

        val locationWeather = LocationWeatherMapper().buildFrom(request.body)


        Log.d("print", locationWeather.name)

        return locationWeather
    }
}