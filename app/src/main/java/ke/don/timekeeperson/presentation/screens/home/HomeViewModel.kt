package ke.don.timekeeperson.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ke.don.timekeeperson.data.model.Timer
import ke.don.timekeeperson.data.repository.TimerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val timerRepository: TimerRepository
): ViewModel() {
    private var _allTimers = MutableStateFlow(emptyList<Timer>())
    val allTimers : StateFlow<List<Timer>> = _allTimers

    init {
        viewModelScope.launch {
            getALlTimers()

        }
    }

    suspend fun getALlTimers(){
        timerRepository.getAllTimers().collect{ timers ->
            _allTimers.update { timers }
        }
    }

}