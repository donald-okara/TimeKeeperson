package ke.don.timekeeperson.data.repository

import ke.don.timekeeperson.data.model.Timer
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    /**
     * CREATE
     */
    suspend fun addTimer(timer: Timer)
    /**
     * READ
     */
    suspend fun getAllTimers(): Flow<List<Timer>>

    suspend fun getAllTimerNames():Flow<List<String>>

    suspend fun getTimerById(id: Int): Flow<Timer?>

    /**
     * UPDATE
     */
    suspend fun updateTimerName(id: Int, name: String)

    /**
     * DELETE
     */
    suspend fun deleteTimer(timer: Timer)
}