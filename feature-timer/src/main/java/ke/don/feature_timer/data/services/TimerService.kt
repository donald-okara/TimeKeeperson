package ke.don.feature_timer.data.services

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import ke.don.datasource.domain.model.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService : Service() {

    companion object {
        const val TIMER_UPDATE_ACTION = "ke.don.timekeeperson.UPDATE"
        const val TIMER_REMAINING_TIME = "remaining_time"
        const val TIMER_TOTAL_DURATION = "total_duration"
        const val ACTION_PAUSE = "pause"
        const val ACTION_RESUME = "resume"
    }

    private var timer: CountDownTimer? = null
    private var remainingTime: Long = 0L
    private var totalDuration: Long = 0L
    private var status: SessionStatus = SessionStatus.IDLE
    private var startTime: Long = 0L
    private var isPaused: Boolean = false
    private var timerJob: Job? = null
    private var timerCallback: TimerCallback? = null

    // Method to set the callback
    fun setTimerCallback(callback: TimerCallback) {
        this.timerCallback = callback
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        println("TimerService: onStartCommand called with action = $action")

        when (action) {
            ACTION_PAUSE -> {
                status = SessionStatus.PAUSED
                println("TimerService: Pausing timer, time remaining: $remainingTime")
                CoroutineScope(Dispatchers.IO).launch{
                    pauseTimer()
                }
            }
            ACTION_RESUME -> {
                status = SessionStatus.RUNNING
                println("TimerService: Resuming timer")
                resumeTimer()
            }
            else -> {
                val durationMillis = intent?.getLongExtra("durationMillis", 0L) ?: 0L
                println("TimerService: Received durationMillis = $durationMillis, isPaused = $isPaused")

                if (!isPaused && durationMillis > 0) {
                    startTimer(durationMillis)
                } else if (isPaused) {
                    println("TimerService: Timer is paused. Broadcasting remaining time.")
                } else {
                    println("TimerService: Invalid durationMillis or timer is already running.")
                }
            }
        }
        return START_NOT_STICKY
    }

    fun startTimer(durationMillis: Long) {
        totalDuration = durationMillis
        remainingTime = totalDuration
        status = SessionStatus.RUNNING
        println("TimerService: Starting timer with durationMillis = $durationMillis")

        // Cancel any existing timer job
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (remainingTime > 0) {
                delay(1000L)
                remainingTime -= 1
                println("TimerService: Timer tick - remainingTime = $remainingTime")
                timerCallback?.onTimerTick(remainingTime, totalDuration, status)
            }

            println("TimerService: Timer finished")
            timerCallback?.onTimerFinished()
        }
    }


    suspend fun pauseTimer() {
        status = SessionStatus.PAUSED
        timerJob?.cancel() // Cancel the timer
        isPaused = true
        timerCallback?.onTimerTick(remainingTime, totalDuration, status)

        println("TimerService: Timer paused at remainingTime = $remainingTime")

    }

    fun resumeTimer() {
        if (remainingTime > 0) {
            status = SessionStatus.RUNNING
            startTimer(remainingTime) // Resume from the remaining time
            isPaused = false
            CoroutineScope(Dispatchers.IO).launch{
                timerCallback?.onTimerTick(remainingTime, totalDuration, status)
            }

            println("TimerService: Timer resumed with remainingTime = $remainingTime")
        }
    }

    fun stopTimer(){
        status = SessionStatus.COMPLETED
        timerJob?.cancel()
        timer = null
        CoroutineScope(Dispatchers.IO).launch{
            timerCallback?.onTimerTick(remainingTime, totalDuration, status)
        }
        println("TimerService: Timer stopped")
    }

    override fun onDestroy() {
        status = SessionStatus.IDLE
        timer?.cancel() // Cancel the timer when the service is destroyed
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null // This is a started service, not a bound service
    }
}
