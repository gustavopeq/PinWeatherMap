package gustavo.projects.pinweathermap.domain.mappers

import gustavo.projects.pinweathermap.domain.models.LocationWeather
import gustavo.projects.pinweathermap.network.response.GetWeatherByCoordResponse

class LocationWeatherMapper {

    fun buildFrom(response: GetWeatherByCoordResponse): LocationWeather {

        return LocationWeather(response.name!!,response.coord.lat, response.coord.lon)
    }
}