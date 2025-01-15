package ke.don.app_home.domain.states

import ke.don.datasource.domain.model.Timer
import ke.don.datasource.domain.utils.reformatDateTime
import ke.don.datasource.domain.utils.reformatDuration


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
    val maxLength = 20 // Maximum character limit

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