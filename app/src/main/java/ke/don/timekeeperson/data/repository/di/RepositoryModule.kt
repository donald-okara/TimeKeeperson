package ke.don.timekeeperson.data.repository.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ke.don.datasource.dao.TimerDao
import ke.don.timekeeperson.data.repository.HomeRepository
import ke.don.timekeeperson.data.repository.HomeRepositoryImpl
import ke.don.timekeeperson.data.repository.TimerRepository
import ke.don.timekeeperson.data.repository.TimerRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideTimerRepository(
        timerDao: TimerDao
    ): TimerRepository = TimerRepositoryImpl(timerDao)

    @Provides
    @Singleton
    fun provideHomeRepository(
        timerRepository: TimerRepository
    ): HomeRepository = HomeRepositoryImpl(timerRepository)


}