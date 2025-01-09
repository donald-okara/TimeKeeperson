package ke.don.timekeeperson.presentation.screens.time_picker

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ke.don.datasource.model.Timer
import ke.don.datasource.model.TimerType
import ke.don.timekeeperson.data.repository.TimerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddTimerViewModel @Inject constructor(
    private val timerRepository: TimerRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            getAllTimerNames()
        }
    }

    private var _name = MutableStateFlow("")
    val name : StateFlow<String> = _name

    private var _hours =  MutableStateFlow(0)
    val hours : StateFlow<Int> = _hours

    private var _minutes = MutableStateFlow(0)
    val minutes : StateFlow<Int> = _minutes

    private var _seconds = MutableStateFlow(0)
    val seconds : StateFlow<Int> = _seconds

    private var _timerType = MutableStateFlow(TimerType.Classic)
    val timerType : StateFlow<TimerType> = _timerType

    private val _timerNames = MutableStateFlow(emptyList<String>())
    val timerNames: StateFlow<List<String>> = _timerNames

    private var _nameError = MutableStateFlow<String?>(null)  // State to hold the error message
    val nameError: StateFlow<String?> = _nameError

    val totalDuration: StateFlow<Int> = combine(hours, minutes, seconds) { hoursValue, minutesValue, secondsValue ->
        hoursValue * 3600 + minutesValue * 60 + secondsValue
    }.stateIn(
        scope = viewModelScope, // Or another CoroutineScope
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val timer: StateFlow<Timer> = combine(
        name,
        timerType,
        totalDuration
    ) { nameValue, timerTypeValue, totalDurationValue ->
        Timer(
            name = nameValue,
            type = timerTypeValue,
            totalDuration = totalDurationValue,
            dateCreated = Date(), // Set as current date
            lastEdited = Date()   // Set as current date
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Timer(
            name = "",
            type = TimerType.Classic,
            totalDuration = 0
        )
    )

    val timerIsValid: StateFlow<Boolean> = combine(
        totalDuration,
        name
    ) { totalDurationValue, nameValue ->
        totalDurationValue > 0 && !timerNames.value.contains(nameValue)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    private fun clearState(){
        _name.update { "" }
        _hours.update { 0 }
        _minutes.update { 0 }
        _seconds.update { 0 }
        _timerType.update {TimerType.Classic }
        _nameError.update { null }
    }

    fun onHourValueChange(newValue: Int){
        _hours.update {
            newValue
        }
    }

    fun onMinuteValueChange(newValue: Int){
        _minutes.update {
            newValue
        }
    }

    fun onSecondValueChange(newValue: Int){
        _seconds.update {
            newValue
        }
    }

    fun onNameValueChange(newValue: String){
        if (timerNames.value.contains(newValue)) {
            _nameError.update { "$newValue is already in the database" } // Set the error message
        } else {
            _nameError.update { null } // Clear the error if name is valid
        }


        _name.update {
            newValue
        }

        // Check if the new name already exists in the list
    }

    private suspend fun getAllTimerNames() {
        // Collect the flow and update the state
        timerRepository.getAllTimerNames().collect { names ->
            _timerNames.update { names }
        }
    }

    fun addTimer(){
        if(timerIsValid.value){
            viewModelScope.launch {
                timerRepository.addTimer(timer.value)
                clearState()
            }
        }


    }

}