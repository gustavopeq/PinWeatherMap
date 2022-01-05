package gustavo.projects.pinweathermap.database

import androidx.room.*

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmark_entity")
    suspend fun getAllBookmarkEntities(): List<BookmarkEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(bookmarkEntity: BookmarkEntity)

    @Delete
    suspend fun removeLocation(bookmarkEntity: BookmarkEntity)
}