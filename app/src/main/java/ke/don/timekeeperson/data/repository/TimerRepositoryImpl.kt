package ke.don.timekeeperson.data.repository

import ke.don.timekeeperson.data.dao.TimerDao
import ke.don.timekeeperson.data.model.Timer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimerRepositoryImpl(
    private val timerDao: TimerDao
): TimerRepository {
    override suspend fun addTimer(timer: Timer) = timerDao.addTimer(timer)

    override suspend fun getAllTimers(): Flow<List<Timer>> = timerDao.getAllTimers()

    override suspend fun getAllTimerNames(): Flow<List<String>> = timerDao.getAllTimerNames()

    override suspend fun getTimerById(id: Int): Flow<Timer?> = timerDao.getTimerById(id)

    override suspend fun updateTimerName(id: Int, name: String) = timerDao.updateTimerName(id = id, name = name)

    override suspend fun deleteTimer(timer: Timer) = timerDao.deleteTimer(timer)

}