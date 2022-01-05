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
    fun provideMapRepository(apiClient: ApiClient)
    = MapRepository(apiClient)
//
//    @Singleton
//    @Provides
//    fun provideBookmarkDao(appDatabase: AppDatabase): BookmarkDao {
//        return appDatabase.bookmarkDao()
//    }
//
//    @Singleton
//    @Provides
//    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
//        return Room.databaseBuilder(
//            appContext,
//            AppDatabase::class.java,
//            "appDatabase").build()
//    }
}