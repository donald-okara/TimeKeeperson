package ke.don.feature_timer.data.services

import ke.don.datasource.domain.model.SessionStatus

interface TimerCallback {
    suspend fun onTimerTick(remainingTime: Long, totalDuration: Long, status: SessionStatus)
    suspend fun onTimerFinished()
}
