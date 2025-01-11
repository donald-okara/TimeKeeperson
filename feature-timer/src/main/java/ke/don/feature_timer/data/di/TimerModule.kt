package ke.don.feature_timer.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ke.don.datasource.dao.SessionDao
import ke.don.datasource.dao.TimerDao
import ke.don.feature_timer.data.repositories.SessionRepositoryImpl
import ke.don.feature_timer.data.repositories.TimerRepositoryImpl
import ke.don.feature_timer.domain.repositories.SessionRepository
import ke.don.feature_timer.domain.repositories.TimerRepository

@Module
@InstallIn(SingletonComponent::class)
object TimerModule {
    @Provides
    fun provideTimerRepository(timerDao: TimerDao): TimerRepository {
        return TimerRepositoryImpl(timerDao)
    }

    @Provides
    fun provideSessionRepository(
        sessionDao: SessionDao,
        @ApplicationContext context: Context
    ): SessionRepository = SessionRepositoryImpl(context =context,sessionDao = sessionDao)

}