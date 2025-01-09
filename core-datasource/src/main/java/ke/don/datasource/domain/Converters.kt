package ke.don.datasource.domain

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    // Convert Long (timestamp) to Date
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    // Convert Date to Long (timestamp)
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
