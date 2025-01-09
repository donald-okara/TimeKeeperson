package ke.don.datasource.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Timer(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: TimerType,
    val totalDuration: Int,
    val dateCreated: Date = Date(),
    val lastEdited: Date = Date()
)

enum class TimerType{
    Classic,
    Stratified,
    Pomodoro
}