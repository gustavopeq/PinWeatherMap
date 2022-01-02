package gustavo.projects.pinweathermap.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gustavo.projects.pinweathermap.domain.repository.MapRepository
import gustavo.projects.pinweathermap.network.ApiClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMapRepository(apiClient: ApiClient) = MapRepository(apiClient)
}