package dvachmovie.db.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.joda.time.LocalDateTime

@Entity(tableName = "movieData")
@TypeConverters(Converters::class)
data class MovieEntity(@PrimaryKey @ColumnInfo(name = "movieUrl") override val movieUrl: String,
                       @ColumnInfo(name = "previewUrl") override val previewUrl: String = "",
                       @ColumnInfo(name = "board") override val board: String = "",
                       @ColumnInfo(name = "isPlayed") override var isPlayed: Boolean = false,
                       @ColumnInfo(name = "date") override var date: LocalDateTime = LocalDateTime(),
                       @ColumnInfo(name = "md5") override val md5: String = "",
                       @ColumnInfo(name = "thread") override val thread: Long = 0,
                       @ColumnInfo(name = "post") override val post: Long = 0
) : Movie {

    companion object {
        private const val uniqueNumber = 31
    }

    override fun equals(other: Any?) =
            other is Movie
                    && movieUrl == other.movieUrl
                    && previewUrl == other.previewUrl
                    && board == other.board

    override fun hashCode(): Int {
        var result = movieUrl.hashCode()
        result = uniqueNumber * result + previewUrl.hashCode()
        result = uniqueNumber * result + board.hashCode()
        result = uniqueNumber * result + isPlayed.hashCode()
        return result
    }
}

interface Movie {
    val movieUrl: String
    val previewUrl: String
    val board: String
    var isPlayed: Boolean
    var date: LocalDateTime
    val md5: String
    val post: Long
    val thread: Long
}
