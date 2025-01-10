package ke.don.app_home.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ke.don.app_home.domain.repositories.HomeRepository
import ke.don.app_home.domain.states.HomeUiState
import ke.don.app_home.domain.states.toTimerUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    val homeUiState: StateFlow<HomeUiState> = homeRepository.getAllTimers()
        .map { timers ->
            HomeUiState(
                timerIsEmpty = timers.isEmpty(),
                allTimers = timers.map { it.toTimerUiState(it) }
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState()
        )
}
