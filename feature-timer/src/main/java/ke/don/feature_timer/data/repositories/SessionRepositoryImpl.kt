package ke.don.feature_timer.data.repositories

import android.content.Context
import android.os.SystemClock
import android.util.Log
import ke.don.datasource.database.dao.SessionDao
import ke.don.datasource.domain.SessionState
import ke.don.datasource.domain.model.SessionStatus
import ke.don.datasource.domain.model.Timer
import ke.don.datasource.domain.toEntity
import ke.don.feature_timer.data.services.TimerCallback
import ke.don.feature_timer.data.services.TimerService
import ke.don.feature_timer.domain.repositories.SessionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SessionRepositoryImpl(
    private val context: Context,
    private val sessionDao: SessionDao,
) : SessionRepository, TimerCallback {

    private val _sessionState = MutableStateFlow(SessionState())
    override val sessionState: StateFlow<SessionState> = _sessionState

    private var initialState = SessionState()

    private var timerService: TimerService? = null


    override suspend fun createSession(
        timerId: Int,
        timerName: String,
        timeLeft: Long,
    ) {
        val newSession = SessionState(
            sessionId = SystemClock.elapsedRealtime().toInt(),
            timerId = timerId,
            timerName = timerName,
            timeLeft = timeLeft,
            expectedRunTime = timeLeft,
        )

        _sessionState.value = newSession  // Set both session state and initial state to the same new session

        initialState = newSession

        // Initialize TimerService with the callback
        timerService = TimerService().apply {
            setTimerCallback(this@SessionRepositoryImpl)
        }


        Log.d("SessionRepositoryImpl", "Session created: $newSession")
    }

    override suspend fun startSession() {
        timerService?.startTimer(_sessionState.value.expectedRunTime)
        _sessionState.update { it.copy(startTime = System.currentTimeMillis()) }
    }

    override suspend fun pauseSession() {
        timerService?.pauseTimer()
    }

    override suspend fun resumeSession() {
        timerService?.resumeTimer()
    }

    override suspend fun stopSession() {
        timerService?.stopTimer()
    }

    override suspend fun saveSession() {
        sessionDao.addSession(sessionState.value.toEntity())
        resetSessionStateOnDestroy()
    }

    override suspend fun cancel() {
        _sessionState.value = initialState  // Reset to default session state
    }

    override suspend fun onTimerTick(remainingTime: Long, totalDuration: Long, status: SessionStatus) {
        _sessionState.update { it.copy(timeLeft = remainingTime, status = status, totalDuration = totalDuration) }
    }

    override suspend fun onTimerFinished() {
        _sessionState.update { it.copy(timeLeft = 0, status = SessionStatus.COMPLETED) }
    }


    // This will reset the session state when the activity or fragment is destroyed
    override suspend fun resetSessionStateOnDestroy() {
        _sessionState.value = SessionState()  // Reset the session state
    }
}
