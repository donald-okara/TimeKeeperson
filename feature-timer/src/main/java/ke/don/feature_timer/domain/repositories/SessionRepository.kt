package ke.don.feature_timer.domain.repositories

import ke.don.datasource.model.Timer
import ke.don.feature_timer.domain.model.SessionState
import kotlinx.coroutines.flow.StateFlow

interface SessionRepository {
    val sessionState: StateFlow<SessionState>

    val initialSessionState: StateFlow<SessionState?>

    fun createSession(
        timer : Timer
    )

    fun startSession()

    fun pauseSession()

    fun resumeSession()

    fun stopSession()

    suspend fun saveSession()

    fun cancel()
}