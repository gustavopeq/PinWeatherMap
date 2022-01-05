package gustavo.projects.pinweathermap.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_entity")
data class BookmarkEntity(
    @PrimaryKey val id: Int,
    val latitude: Double,
    val longitude: Double
)
