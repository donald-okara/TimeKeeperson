package ke.don.datasource.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ke.don.datasource.model.Timer
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {
    /**
     * CREATE
     */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTimer(timer: Timer)

    /**
     * READ
     */
    @Query("SELECT * FROM timer")
    fun getAllTimers(): Flow<List<Timer>>

    @Query("SELECT name FROM timer")
    fun getAllTimerNames(): Flow<List<String>>

    @Query("SELECT * FROM timer WHERE id = :id")
    suspend fun getTimerById(id: Int): Timer?

    /**
     * UPDATE
     */
    @Query("UPDATE timer SET name = :name WHERE id = :id")
    fun updateTimerName(id: Int, name: String)

    /**
     * DELETE
     */
    @Delete
    fun deleteTimer(timer: Timer)
}