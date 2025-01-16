package ke.don.feature_timer.domain.repositories

import ke.don.datasource.domain.model.Timer
import ke.don.datasource.domain.SessionState
import kotlinx.coroutines.flow.StateFlow

interface SessionRepository {
    val sessionState: StateFlow<SessionState>

    suspend fun createSession(
        timerId: Int,
        timerName: String,
        timeLeft: Long
    )

    suspend fun startSession()

    suspend fun pauseSession()

    suspend fun resumeSession()

    suspend fun stopSession()

    suspend fun saveSession()

    suspend fun cancel()

    suspend fun resetSessionStateOnDestroy()
}