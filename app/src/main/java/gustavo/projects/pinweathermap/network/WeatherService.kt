package gustavo.projects.pinweathermap.network

import gustavo.projects.pinweathermap.Constants
import gustavo.projects.pinweathermap.network.response.GetWeatherByCoordResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather?&appid=${Constants.OPEN_WEATHERMAP_API_KEY}&units=metric")
    suspend fun getWeatherByCoord(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Response<GetWeatherByCoordResponse>
}