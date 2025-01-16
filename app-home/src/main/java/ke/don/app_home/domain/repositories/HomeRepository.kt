package ke.don.app_home.domain.repositories

import ke.don.app_home.domain.states.HomeUiState
import ke.don.datasource.domain.SessionState
import ke.don.datasource.domain.model.Timer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface HomeRepository {

    val homeUiState : StateFlow<HomeUiState>

    suspend fun createSession(
        timerId: Int,
        timerName: String,
        timeLeft: Long,
    )

    suspend fun startSession()

    suspend fun pauseSession()

    suspend fun resumeSession()

    suspend fun stopSession()

    suspend fun saveSession()

    suspend fun cancel()

    suspend fun resetSessionStateOnDestroy()

}