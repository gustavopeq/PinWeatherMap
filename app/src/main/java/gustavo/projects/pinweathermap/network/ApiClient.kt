package gustavo.projects.pinweathermap.network

import gustavo.projects.pinweathermap.network.response.GetWeatherByCoordResponse
import retrofit2.Response

class ApiClient(
    private val weatherService: WeatherService
) {

    suspend fun getWeatherByCoord(lat: Double, lon: Double) : SimpleResponse<GetWeatherByCoordResponse> {
        return safeApiCall { weatherService.getWeatherByCoord(lat,lon) }
    }

    private inline fun <T> safeApiCall(apiCall: () -> Response<T>) : SimpleResponse<T> {
        return try {
            SimpleResponse.success(apiCall.invoke())
        }catch (e: Exception){
            SimpleResponse.failure(e)
        }
    }
}