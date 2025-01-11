package ke.don.feature_timer.data.services

import ke.don.datasource.model.SessionStatus

interface TimerCallback {
    fun onTimerTick(remainingTime: Long, totalDuration: Long, status: SessionStatus)
    fun onTimerFinished()
}
