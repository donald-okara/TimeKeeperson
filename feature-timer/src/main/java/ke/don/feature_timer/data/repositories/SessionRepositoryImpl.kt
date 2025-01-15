package ke.don.feature_timer.data.repositories

import android.content.Context
import android.os.SystemClock
import ke.don.datasource.database.dao.SessionDao
import ke.don.datasource.datastore.sessionStateDataStore
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

    private val _initialSessionState = MutableStateFlow<SessionState?>(null)
    override val initialSessionState: StateFlow<SessionState?> = _initialSessionState

    private var timerService: TimerService? = null

    init {
        // Launch a coroutine to load the persisted session state
        CoroutineScope(Dispatchers.IO).launch{
            // Load the persisted session state
            loadSessionState()
        }

    }

    private suspend fun loadSessionState() {
        context.sessionStateDataStore.data.map {
            it
        }.collect { savedState ->
            _sessionState.value = savedState
            _initialSessionState.value = savedState
        }
    }

    override suspend fun createSession(timer: Timer) {
        val newSession = SessionState(
            sessionId = SystemClock.elapsedRealtime().toInt(),
            timerId = timer.id,
            timerName = timer.name,
            timeLeft = timer.totalDuration.toLong(),
            expectedRunTime = timer.totalDuration.toLong(),
        )
        _initialSessionState.update { newSession }
        _sessionState.update { newSession }

        // Initialize TimerService with the callback
        timerService = TimerService().apply {
            setTimerCallback(this@SessionRepositoryImpl)
        }

        // Save session state
        saveSessionState(newSession)
    }

    override suspend fun startSession() {
        timerService?.startTimer(_sessionState.value.expectedRunTime)
        _sessionState.update { it.copy(startTime = System.currentTimeMillis()) }
        saveSessionState(_sessionState.value)
    }

    override suspend fun pauseSession() {
        timerService?.pauseTimer()
        saveSessionState(_sessionState.value)
    }

    override suspend fun resumeSession() {
        timerService?.resumeTimer()
        saveSessionState(_sessionState.value)
    }

    override suspend fun stopSession() {
        timerService?.stopTimer()
        saveSessionState(_sessionState.value)
    }

    override suspend fun saveSession() {
        sessionDao.addSession(sessionState.value.toEntity())
        _sessionState.update { _initialSessionState.value!! }
        saveSessionState(_sessionState.value)
    }

    override suspend fun cancel() {
        _sessionState.update { _initialSessionState.value!! }

        //Reset datastore sessionState
        saveSessionState(SessionState())
    }

    override suspend fun onTimerTick(remainingTime: Long, totalDuration: Long, status: SessionStatus) {
        _sessionState.update { it.copy(timeLeft = remainingTime, status = status) }
        saveSessionState(_sessionState.value)
    }

    override suspend fun onTimerFinished() {
        _sessionState.update { it.copy(timeLeft = 0, status = SessionStatus.COMPLETED) }
        saveSessionState(_sessionState.value)
    }

    private suspend fun saveSessionState(state: SessionState) {
        context.sessionStateDataStore.updateData { state }
    }
}
