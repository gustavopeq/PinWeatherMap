package gustavo.projects.pinweathermap.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gustavo.projects.pinweathermap.database.AppDatabase
import gustavo.projects.pinweathermap.database.BookmarkDao
import gustavo.projects.pinweathermap.domain.repository.MapRepository
import gustavo.projects.pinweathermap.network.ApiClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMapRepository(apiClient: ApiClient, bookmarkDao: BookmarkDao)
    = MapRepository(apiClient, bookmarkDao)

    @Singleton
    @Provides
    fun provideBookmarkDao(appDatabase: AppDatabase): BookmarkDao {
        return appDatabase.bookmarkDao()
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "pin_weathermap_database").build()
    }
}