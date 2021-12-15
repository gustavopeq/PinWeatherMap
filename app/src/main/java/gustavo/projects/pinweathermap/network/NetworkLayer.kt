package gustavo.projects.pinweathermap.network

import com.google.gson.GsonBuilder
import gustavo.projects.pinweathermap.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkLayer {

    val gson = GsonBuilder().create()

    val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL_WEATHER_API)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val weatherService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }

    val apiClient = ApiClient(weatherService)

}