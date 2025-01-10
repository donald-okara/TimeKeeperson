package ke.don.app_home.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ke.don.app_home.data.repositories.HomeRepositoryImpl
import ke.don.app_home.domain.repositories.HomeRepository
import ke.don.feature_timer.domain.repositories.TimerRepository

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    fun provideHomeRepository(
        timerRepository: TimerRepository
    ): HomeRepository {
        return HomeRepositoryImpl(timerRepository)
    }

}