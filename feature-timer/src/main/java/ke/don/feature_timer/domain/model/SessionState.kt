package ke.don.feature_timer.domain.model

import ke.don.datasource.model.Session
import ke.don.datasource.model.SessionStatus

data class SessionState(
    val sessionId: Int= 0,
    val timerId: Int = 0,
    val timerName: String = "",
    val startTime: Long = 0L,
    val endTime: Long = 0L,
    val expectedRunTime: Long = 0L,
    val duration: Long = 0L,  // Time spent during the current session
    val totalDuration: Long = 0L,  // Total time accumulated over multiple sessions
    val timeLeft: Long = 0L,  // Remaining time in the current session (mapped from the timer)
    val status: SessionStatus = SessionStatus.IDLE
)

fun SessionState.toEntity() = Session(
    sessionId = this.sessionId,
    timerId = this.timerId,
    timerName = this.timerName,
    startTime = this.startTime,
    endTime = this.endTime,
    totalDuration = this.totalDuration,
    expectedRunTime = this.expectedRunTime,
    status = this.status
)
