package gustavo.projects.pinweathermap.domain.repository


import gustavo.projects.pinweathermap.database.BookmarkDao
import gustavo.projects.pinweathermap.database.BookmarkEntity
import gustavo.projects.pinweathermap.domain.mapper.LocationWeatherMapper
import gustavo.projects.pinweathermap.domain.model.LocationWeather
import gustavo.projects.pinweathermap.network.ApiClient

class MapRepository(
    private val apiClient: ApiClient,
    private val bookmarkDao: BookmarkDao
) {

    suspend fun getWeatherByCoord(lat: Double, lon: Double): LocationWeather? {

        val request = apiClient.getWeatherByCoord(lat, lon)

        if (request.failed || !request.isSuccessful) {
            return null
        }

        return LocationWeatherMapper().buildFrom(request.body)
    }

    suspend fun addBookmarkToDatabase(locationWeather: LocationWeather) {
        val newBookmark = BookmarkEntity(
            locationWeather.id,
            locationWeather.latitude,
            locationWeather.longitude)

        bookmarkDao.insertLocation(newBookmark)
    }

    suspend fun removeBookmarkFromDatabase(locationWeather: LocationWeather) {
        val bookmarkToRemove = BookmarkEntity(
            locationWeather.id,
            locationWeather.latitude,
            locationWeather.longitude
        )

        bookmarkDao.removeLocation(bookmarkToRemove)
    }

    suspend fun getAllBookmarksFromDatabase(): List<BookmarkEntity> {
        return bookmarkDao.getAllBookmarkEntities()
    }
}