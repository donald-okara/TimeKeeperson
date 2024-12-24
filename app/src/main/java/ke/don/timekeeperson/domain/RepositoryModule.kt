package ke.don.timekeeperson.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ke.don.timekeeperson.data.dao.TimerDao
import ke.don.timekeeperson.data.repository.TimerRepository
import ke.don.timekeeperson.data.repository.TimerRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRepository(
        timerDao: TimerDao
    ): TimerRepository = TimerRepositoryImpl(timerDao)

}