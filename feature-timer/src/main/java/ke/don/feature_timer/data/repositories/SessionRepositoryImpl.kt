package ke.don.feature_timer.data.repositories

import android.content.Context
import android.os.SystemClock
import ke.don.datasource.dao.SessionDao
import ke.don.datasource.model.SessionStatus
import ke.don.datasource.model.Timer
import ke.don.feature_timer.data.services.TimerCallback
import ke.don.feature_timer.data.services.TimerService
import ke.don.feature_timer.domain.model.SessionState
import ke.don.feature_timer.domain.model.toEntity
import ke.don.feature_timer.domain.repositories.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SessionRepositoryImpl(
    private val context: Context,
    private val sessionDao: SessionDao
) : SessionRepository, TimerCallback {

    private val _sessionState = MutableStateFlow(SessionState())

    override val sessionState: StateFlow<SessionState> = _sessionState

    private val _initialSessionState = MutableStateFlow<SessionState?>(null)
    override val initialSessionState: StateFlow<SessionState?> = _initialSessionState

    private var timerService: TimerService? = null

    override fun createSession(timer: Timer) {
        val newSession = SessionState(
            sessionId = SystemClock.elapsedRealtime().toInt(),
            timerId = timer.id,
            timerName = timer.name,
            timeLeft = timer.totalDuration.toLong(),
            expectedRunTime = timer.totalDuration.toLong(),
        )
        _initialSessionState.update {
            newSession
        }
        _sessionState.update {
            newSession
        }

        // Initialize TimerService with the callback
        timerService = TimerService().apply {
            setTimerCallback(this@SessionRepositoryImpl)
        }
    }

    override fun startSession() {
        // Start the timer directly through the service
        timerService?.startTimer(_sessionState.value.expectedRunTime)

        // Update session state to running
        _sessionState.update {
            it.copy(startTime = System.currentTimeMillis())
        }
        println("SessionRepositoryImpl: startSession() called with currentSession: ${_sessionState.value}")

    }

    override fun pauseSession() {
        timerService?.pauseTimer()

    }

    override fun resumeSession() {
        timerService?.resumeTimer()

    }

    override fun stopSession() {
        timerService?.stopTimer()

    }

    override suspend fun saveSession() {
        // Save the session data to your database or persistent storage
        sessionDao.addSession(sessionState.value.toEntity())
        _sessionState.update {
            _initialSessionState.value!!
        }
    }

    override fun cancel() {
        _sessionState.update {
            _initialSessionState.value!!
        }
    }

    // TimerCallback interface implementation
    override fun onTimerTick(remainingTime: Long, totalDuration: Long, status: SessionStatus) {
        _sessionState.update {
            it.copy(timeLeft = remainingTime, totalDuration = totalDuration, status = status)
        }
    }

    override fun onTimerFinished() {
        _sessionState.update {
            it.copy(timeLeft = 0, status = SessionStatus.COMPLETED)
        }

    }
}