package gustavo.projects.pinweathermap.domain.mapper

import gustavo.projects.pinweathermap.domain.model.LocationWeather
import gustavo.projects.pinweathermap.network.response.GetWeatherByCoordResponse

class LocationWeatherMapper {

    fun buildFrom(response: GetWeatherByCoordResponse): LocationWeather {

        return LocationWeather(
                response.name,
                response.coord.lat,
                response.coord.lon,
                response.main.temp,
                response.main.feels_like,
                response.main.temp_min,
                response.main.temp_max,
                response.main.humidity,
                response.main.pressure,
                response.dt,
                response.sys.sunrise,
                response.sys.sunset,
                response.wind.speed,
                response.weather[0].main,
                response.weather[0].description,
                response.clouds.all
                )
    }
}