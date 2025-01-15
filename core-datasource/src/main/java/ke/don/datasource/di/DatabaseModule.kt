package ke.don.datasource.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ke.don.datasource.database.dao.SessionDao
import ke.don.datasource.database.dao.TimerDao
import ke.don.datasource.database.TimerKeepersonDatabase
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
        )
            //.addMigrations(MIGRATION_1_3)  // Add the migration here
            .build()
    }

    @Provides
    fun provideTimerDao(database: TimerKeepersonDatabase): TimerDao = database.timerDao()


    @Provides
    fun provideSessionDao(database: TimerKeepersonDatabase): SessionDao = database.sessionDao()

}