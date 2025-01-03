package ke.don.timekeeperson.domain

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ke.don.timekeeperson.data.dao.TimerDao
import ke.don.timekeeperson.data.database.TimerKeepersonDatabase
import ke.don.timekeeperson.data.repository.TimerRepository
import ke.don.timekeeperson.data.repository.TimerRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TimerKeepersonDatabase {
        return Room.databaseBuilder(
            context,
            TimerKeepersonDatabase::class.java,
            "timekeeperson_database"
        ).build()
    }

    @Provides
    fun provideTimerDao(database: TimerKeepersonDatabase): TimerDao {
        return database.timerDao()
    }

}