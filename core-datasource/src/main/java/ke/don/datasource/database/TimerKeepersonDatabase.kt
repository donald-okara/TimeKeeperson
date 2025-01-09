package ke.don.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ke.don.datasource.dao.TimerDao
import ke.don.datasource.domain.Converters
import ke.don.datasource.model.Timer

@Database(
    entities = [Timer::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)  // Register the converters here
abstract class TimerKeepersonDatabase : RoomDatabase() {

    abstract fun timerDao(): TimerDao

    companion object {
        @Volatile
        private var Instance : TimerKeepersonDatabase? = null

        fun getDatabase(context: Context): TimerKeepersonDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TimerKeepersonDatabase::class.java, "timekeeperson_database")
                    .build()
                    .also { Instance = it }
            }

        }

    }

}