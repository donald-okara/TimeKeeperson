package ke.don.feature_timer.presentation.screens.timer_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ke.don.core_domain.screens.Screens
import ke.don.datasource.model.Timer
import ke.don.feature_timer.domain.repositories.TimerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TimeDetailsViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private var timerId = savedStateHandle.get<String>(Screens.TimerDetails.timerIdNavigationArgument)?.toInt()

    // Flow to emit timer data
    val timer: StateFlow<Timer?> = timerId?.let {
        timerRepository.getTimerById(it)
            .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            null // Initial value is null while loading
        )
    } ?: MutableStateFlow(null)
}