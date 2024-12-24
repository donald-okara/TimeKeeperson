package ke.don.timekeeperson.presentation.screens.timer

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var _initialTime = 60
    val initialTime: Int
        get() = _initialTime

    private val _timeRemaining = MutableStateFlow(
        savedStateHandle.get<Int>("timeRemaining") ?: _initialTime
    )
    val timeRemaining: StateFlow<Int> = _timeRemaining

    private var _timerRunning = MutableStateFlow(false)
    val timerRunning: StateFlow<Boolean> = _timerRunning

    private var timerJob: Job? = null  // Track the job to cancel when paused

    // Derived progress as a fraction (0f to 1f)
    val progress: StateFlow<Float> = _timeRemaining.map { remaining ->
        remaining.toFloat() / _initialTime
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 1f
    )

    val elapsed : StateFlow<Boolean> = progress.map {
        it < 0.3f
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )

    fun setTimer(time: Int) {
        _timeRemaining.value = time // Properly initialize _timeRemaining
        _timerRunning.value = false // Reset timer state
    }

    fun onTimerClicked() {
        if (_timerRunning.value) {
            pauseTimer()  // Pause if it's already running
         } else {
            resumeTimer()  // Resume if paused
        }
    }

    fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_timeRemaining.value > 0) {
                delay(1000L)
                if (!_timerRunning.value) {
                    return@launch  // Stop the loop if the timer is paused
                }
                _timeRemaining.value -= 1
                savedStateHandle["timeRemaining"] = _timeRemaining.value // Save state on each update

            }
            _timerRunning.value = false
        }
    }

    private fun pauseTimer() {
        _timerRunning.value = false
        timerJob?.cancel()  // Cancel the ongoing timer job
        Log.d("TimerViewModel", "Timer paused")
    }

    private fun resumeTimer() {
        _timerRunning.value = true
        Log.d("TimerViewModel", "Timer resumed")
        startTimer()  // Resume the timer from where it was paused
    }

    private fun resetTimer() {
        _timeRemaining.value = _initialTime
        pauseTimer()  // Also pause the timer when reset
    }

    fun stopTimer() {
        _timerRunning.value = false
        resetTimer()
    }
}
