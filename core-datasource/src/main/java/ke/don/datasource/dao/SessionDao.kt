package ke.don.datasource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ke.don.datasource.model.Session

@Dao
interface SessionDao {
    /**
     * CREATE
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSession(session: Session)


    /**
     * READ
     */

    /**
     * UPDATE
     */

    /**
     * DELETE
     */

}