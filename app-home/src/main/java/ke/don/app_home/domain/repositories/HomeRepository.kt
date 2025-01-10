package ke.don.app_home.domain.repositories

import ke.don.datasource.model.Timer
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getAllTimers(): Flow<List<Timer>>

}