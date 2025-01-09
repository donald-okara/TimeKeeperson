package ke.don.timekeeperson.data.repository

import ke.don.datasource.model.Timer
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl(
    private val timerRepository: TimerRepository
): HomeRepository {
    override fun getAllTimers(): Flow<List<Timer>> = timerRepository.getAllTimers()

}