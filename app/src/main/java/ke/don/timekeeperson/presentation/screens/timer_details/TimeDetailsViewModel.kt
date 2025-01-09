package ke.don.timekeeperson.presentation.screens.timer_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ke.don.datasource.model.Timer
import ke.don.timekeeperson.data.repository.TimerRepository
import ke.don.timekeeperson.presentation.navigation.Screens
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