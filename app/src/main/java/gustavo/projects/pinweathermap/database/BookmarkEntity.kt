package gustavo.projects.pinweathermap.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmark_entity")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String
)
