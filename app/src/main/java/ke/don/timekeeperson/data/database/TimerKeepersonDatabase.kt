package ke.don.timekeeperson.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ke.don.timekeeperson.data.dao.TimerDao
import ke.don.timekeeperson.data.model.Timer
import ke.don.timekeeperson.domain.Converters

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

        fun getDatabase(context: Context): TimerKeepersonDatabase{
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, TimerKeepersonDatabase::class.java, "timekeeperson_database")
                    .build()
                    .also { Instance = it }
            }

        }

    }

}