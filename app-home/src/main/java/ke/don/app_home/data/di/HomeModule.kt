package ke.don.app_home.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ke.don.app_home.data.repositories.HomeRepositoryImpl
import ke.don.app_home.domain.repositories.HomeRepository
import ke.don.feature_timer.domain.repositories.SessionRepository
import ke.don.feature_timer.domain.repositories.TimerRepository

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {
    @Provides
    fun provideHomeRepository(
        timerRepository: TimerRepository,
        sessionRepository: SessionRepository,
        @ApplicationContext context: Context
    ): HomeRepository = HomeRepositoryImpl(context = context, sessionRepository = sessionRepository,timerRepository = timerRepository)


}