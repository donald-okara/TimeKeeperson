package ke.don.feature_timer.presentation.screens.timer_session

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ke.don.core_domain.logger.LoggerImpl
import ke.don.core_domain.screens.Screens
import ke.don.datasource.domain.model.Timer
import ke.don.feature_timer.domain.repositories.SessionRepository
import ke.don.feature_timer.domain.repositories.TimerRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.logging.Logger
import javax.inject.Inject

@HiltViewModel
class TimerSessionViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val sessionRepository: SessionRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private var timerId = savedStateHandle.get<String>(Screens.TimerDetails.timerIdNavigationArgument)?.toInt()

    // Flow to emit timer data
    private var _timer = MutableStateFlow<Timer?>(null)
    val timer: StateFlow<Timer?> = _timer


    val sessionState = sessionRepository.sessionState

    init {
        timerId?.let { id ->
            viewModelScope.launch {
                _timer.value = timerRepository.getTimerById(id)
            }
        }

        viewModelScope.launch {
            _timer.collect { fetchedTimer ->
                if (sessionState.value.timerId != fetchedTimer?.id) {
                    fetchedTimer?.let { sessionRepository.createSession(
                        timerId = it.id,
                        timerName = it.name,
                        timeLeft = it.totalDuration.toLong()
                    ) }
                }
            }
        }


        Log.d("TimerSessionViewModel", "$sessionState")

    }


    fun startSession() = viewModelScope.launch {
        sessionRepository.startSession()
    }
    fun pauseSession() = viewModelScope.launch {
        sessionRepository.pauseSession()
    }
    fun resumeSession() = viewModelScope.launch{
        sessionRepository.resumeSession()
    }
    fun stopSession() = viewModelScope.launch{
        sessionRepository.stopSession()
    }
    fun saveSession() = viewModelScope.launch {
        sessionRepository.saveSession()
    }
    fun cancel() = viewModelScope.launch{
        sessionRepository.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            // Reset session state when ViewModel is cleared (activity/fragment destroyed)
            sessionRepository.resetSessionStateOnDestroy()
        }
    }

}