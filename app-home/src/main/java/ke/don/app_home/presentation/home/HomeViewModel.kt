package ke.don.app_home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ke.don.app_home.domain.repositories.HomeRepository
import ke.don.app_home.domain.states.HomeUiState
import ke.don.app_home.domain.states.TimerUiState
import ke.don.app_home.domain.states.toTimerUiState
import ke.don.datasource.domain.model.Timer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> = homeRepository.homeUiState

    private val _selectedTimerId = MutableStateFlow<Int?>(null)
    val selectedTimerId: StateFlow<Int?> = _selectedTimerId

    private val _selectedTimer = MutableStateFlow<TimerUiState?>(null)
    val selectedTimer: StateFlow<TimerUiState?> = _selectedTimer

    fun onSelectTimer(timer: TimerUiState) {
        _selectedTimer.update {
            timer
        }

        _selectedTimerId.update {
            if (it == timer.id) null else timer.id
        }

        viewModelScope.launch{
            homeRepository.createSession(
                timerId = timer.id,
                timerName = timer.name,
                timeLeft = timer.totalDuration.toLong()
            )
        }
    }

    fun onStartTimer() = viewModelScope.launch{ homeRepository.startSession() }

    fun onPauseTimer() = viewModelScope.launch{ homeRepository.pauseSession() }

    fun onResumeTimer() = viewModelScope.launch{ homeRepository.resumeSession() }

    fun onStopTimer() = viewModelScope.launch{ homeRepository.stopSession() }

    fun onSaveSession() = viewModelScope.launch{ homeRepository.saveSession() }

    fun onCancel() = viewModelScope.launch{ homeRepository.cancel() }

    fun onResetSessionStateOnDestroy() = viewModelScope.launch{ homeRepository.resetSessionStateOnDestroy() }


}
