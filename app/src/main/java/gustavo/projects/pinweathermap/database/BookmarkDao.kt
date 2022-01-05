package gustavo.projects.pinweathermap.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM bookmark_entity")
    suspend fun getAllBookmarkEntities(): List<BookmarkEntity>

    @Insert
    suspend fun insert(bookmarkEntity: BookmarkEntity)
}