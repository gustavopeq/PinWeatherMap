package gustavo.projects.pinweathermap.domain.repository

import android.util.Log
import gustavo.projects.pinweathermap.domain.mapper.LocationWeatherMapper
import gustavo.projects.pinweathermap.network.NetworkLayer
import java.util.*

class MapRepository {

    suspend fun getWeatherByCoord(lat: Double, lon: Double): Unit? {

        val request = NetworkLayer.apiClient.getWeatherByCoord(lat,lon)

        if(request.failed || !request.isSuccessful) {
            return null
        }

        val response = LocationWeatherMapper().buildFrom(request.body)


        Log.d("print", response.temp.toString())

        return null
    }
}