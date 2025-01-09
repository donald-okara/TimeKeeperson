package ke.don.timekeeperson.data.repository

import ke.don.datasource.model.Timer
import kotlinx.coroutines.flow.Flow

interface TimerRepository {
    /**
     * CREATE
     */
    suspend fun addTimer(timer: ke.don.datasource.model.Timer)
    /**
     * READ
     */
    fun getAllTimers(): Flow<List<ke.don.datasource.model.Timer>>

    fun getAllTimerNames():Flow<List<String>>

    fun getTimerById(id: Int): Flow<ke.don.datasource.model.Timer?>

    /**
     * UPDATE
     */
    fun updateTimerName(id: Int, name: String)

    /**
     * DELETE
     */
    fun deleteTimer(timer: ke.don.datasource.model.Timer)
}