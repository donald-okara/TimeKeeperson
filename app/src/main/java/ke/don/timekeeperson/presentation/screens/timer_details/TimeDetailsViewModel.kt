package ke.don.timekeeperson.presentation.screens.timer_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ke.don.timekeeperson.data.repository.TimerRepository
import ke.don.timekeeperson.presentation.navigation.Screens
import javax.inject.Inject

@HiltViewModel
class TimeDetailsViewModel @Inject constructor(
    private val timerRepository: TimerRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    private var timerId = savedStateHandle.get<String>(Screens.TimerDetails.timerIdNavigationArgument)


}