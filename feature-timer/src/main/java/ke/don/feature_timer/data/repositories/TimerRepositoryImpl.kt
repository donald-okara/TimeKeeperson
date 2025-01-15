package ke.don.feature_timer.data.repositories

import ke.don.datasource.database.dao.TimerDao
import ke.don.datasource.domain.model.Timer
import ke.don.feature_timer.domain.repositories.TimerRepository
import kotlinx.coroutines.flow.Flow

class TimerRepositoryImpl(
    private val timerDao: TimerDao
): TimerRepository {
    override suspend fun addTimer(timer: Timer) = timerDao.addTimer(timer)

    override fun getAllTimers(): Flow<List<Timer>> = timerDao.getAllTimers()

    override fun getAllTimerNames(): Flow<List<String>> = timerDao.getAllTimerNames()

    override suspend fun getTimerById(id: Int): Timer? = timerDao.getTimerById(id)

    override fun updateTimerName(id: Int, name: String) = timerDao.updateTimerName(id = id, name = name)

    override fun deleteTimer(timer: Timer) = timerDao.deleteTimer(timer)

}