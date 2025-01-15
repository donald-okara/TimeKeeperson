package ke.don.app_home.data.repositories

import ke.don.app_home.domain.repositories.HomeRepository
import ke.don.datasource.domain.model.Timer
import ke.don.feature_timer.domain.repositories.TimerRepository
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl(
    private val timerRepository: TimerRepository
): HomeRepository {
    override fun getAllTimers(): Flow<List<Timer>> = timerRepository.getAllTimers()

}