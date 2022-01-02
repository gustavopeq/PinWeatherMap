package gustavo.projects.pinweathermap.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gustavo.projects.pinweathermap.Constants
import gustavo.projects.pinweathermap.network.ApiClient
import gustavo.projects.pinweathermap.network.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideWeatherService(): WeatherService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_WEATHER_API)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WeatherService::class.java)
    }

    @Singleton
    @Provides
    fun provideApiClient(weatherService: WeatherService): ApiClient {
        return ApiClient(weatherService)
    }
}