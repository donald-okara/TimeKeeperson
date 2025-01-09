package ke.don.timekeeperson.data.repository

import ke.don.datasource.dao.TimerDao
import ke.don.datasource.model.Timer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TimerRepositoryImpl(
    private val timerDao: ke.don.datasource.dao.TimerDao
): TimerRepository {
    override suspend fun addTimer(timer: ke.don.datasource.model.Timer) = timerDao.addTimer(timer)

    override fun getAllTimers(): Flow<List<ke.don.datasource.model.Timer>> = timerDao.getAllTimers()

    override fun getAllTimerNames(): Flow<List<String>> = timerDao.getAllTimerNames()

    override fun getTimerById(id: Int): Flow<ke.don.datasource.model.Timer?> = timerDao.getTimerById(id)

    override fun updateTimerName(id: Int, name: String) = timerDao.updateTimerName(id = id, name = name)

    override fun deleteTimer(timer: ke.don.datasource.model.Timer) = timerDao.deleteTimer(timer)

}