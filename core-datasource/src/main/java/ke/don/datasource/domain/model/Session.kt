package ke.don.datasource.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Session(
    @PrimaryKey(autoGenerate = true)
    val sessionId: Int,
    val timerId: Int,
    val timerName: String,
    val startTime: Long,
    val endTime: Long,
    val expectedRunTime: Long,
    val totalDuration: Long,
    val status: SessionStatus
)

enum class SessionStatus {
    IDLE,
    RUNNING,
    PAUSED,
    COMPLETED
}
