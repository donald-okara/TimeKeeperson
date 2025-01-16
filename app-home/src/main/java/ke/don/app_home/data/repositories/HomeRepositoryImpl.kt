package ke.don.app_home.data.repositories

import android.content.Context
import android.util.Log
import ke.don.app_home.domain.repositories.HomeRepository
import ke.don.app_home.domain.states.HomeUiState
import ke.don.app_home.domain.states.toTimerUiState
import ke.don.feature_timer.domain.repositories.SessionRepository
import ke.don.feature_timer.domain.repositories.TimerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class HomeRepositoryImpl(
    private val timerRepository: TimerRepository,
    private val sessionRepository: SessionRepository,
    private val context: Context
): HomeRepository {
    private val repositoryScope = CoroutineScope(Dispatchers.Default)

    override val homeUiState: StateFlow<HomeUiState> = combine(
        timerRepository.getAllTimers(),
        sessionRepository.sessionState
    ) { timers, sessionState ->
        HomeUiState(
            timerIsEmpty = timers.isEmpty(),
            sessionState = sessionState,
            allTimers = timers.map { it.toTimerUiState(it) }
        )
    }.stateIn(
        scope = repositoryScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )


    override suspend fun createSession(
        timerId: Int,
        timerName: String,
        timeLeft: Long
    ) {
        sessionRepository.createSession(
            timerId = timerId,
            timerName = timerName,
            timeLeft = timeLeft

        )
        Log.d("HomeRepositoryImpl", "HomeState created: ${homeUiState.value}")
    }

    override suspend fun startSession() = sessionRepository.startSession()

    override suspend fun pauseSession() = sessionRepository.pauseSession()

    override suspend fun resumeSession() = sessionRepository.resumeSession()

    override suspend fun stopSession() = sessionRepository.stopSession()

    override suspend fun saveSession() = sessionRepository.saveSession()

    override suspend fun cancel() = sessionRepository.cancel()

    override suspend fun resetSessionStateOnDestroy() = sessionRepository.resetSessionStateOnDestroy()


}