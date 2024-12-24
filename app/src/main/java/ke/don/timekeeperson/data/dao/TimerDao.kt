package ke.don.timekeeperson.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ke.don.timekeeperson.data.model.Timer
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
    fun getTimerById(id: Int): Flow<Timer?>

    /**
     * UPDATE
     */
    @Query("UPDATE timer SET name = :name WHERE id = :id")
    suspend fun updateTimerName(id: Int, name: String)

    /**
     * DELETE
     */
    @Delete
    suspend fun deleteTimer(timer: Timer)
}