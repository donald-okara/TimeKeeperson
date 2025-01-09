package ke.don.timekeeperson.presentation.screens.home

import ke.don.datasource.model.Timer
import ke.don.timekeeperson.presentation.utils.reformatDateTime
import ke.don.timekeeperson.presentation.utils.reformatDuration


data class HomeUiState(
    val timerIsEmpty: Boolean = true,
    val allTimers: List<TimerUiState> = emptyList()
)

data class TimerUiState(
    val id: Int = 0,
    val dateCreatedOrLastEdited: String = "",
    val name: String = "",
    val totalDuration: String = "",
    val type: String = ""
)

fun Timer.toTimerUiState(timer: Timer): TimerUiState {
    val date = if (timer.lastEdited == timer.dateCreated) timer.dateCreated else timer.lastEdited
    val formattedDate = reformatDateTime(date)
    val supportingDateText = if (timer.lastEdited == timer.dateCreated) "Created $formattedDate" else "Last edited $formattedDate"

    val formattedTime = reformatDuration(timer.totalDuration)
    val maxLength = 10 // Maximum character limit

    val displayText = if (timer.name.length > maxLength) {
        "${timer.name.take(maxLength)}..."
    } else {
        timer.name
    }
    return TimerUiState(
        id = timer.id,
        dateCreatedOrLastEdited = supportingDateText,
        name = displayText,
        totalDuration = formattedTime,
        type = timer.type.name
    )

}