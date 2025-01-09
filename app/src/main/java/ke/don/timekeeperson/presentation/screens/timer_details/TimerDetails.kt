package ke.don.timekeeperson.presentation.screens.timer_details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TimerDetailsRoute(
    viewModel: TimeDetailsViewModel = hiltViewModel(),
){
    val timer by viewModel.timer.collectAsState()

    timer?.let {
        TimerDetailsScreen(
            timerName = it.name,
            isTimerRunning = false,
            timeLeft = it.totalDuration,
            modifier = Modifier
        )
    }


}

@Composable
fun TimerDetailsScreen(
    modifier: Modifier = Modifier,
    isTimerRunning: Boolean = false,
    timeLeft: Int = 60,
    timerName: String,
){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = timerName,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        TimerClock(
            progress = 0f,
            isTimerRunning = true,
            timeLeft = timeLeft
        )

    }

}

@Composable
fun TimerClock(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    timeLeft: Int = 0,
    isTimerRunning: Boolean,
    circleColor: Color = MaterialTheme.colorScheme.surface,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Float = 16f,
    fixedSize: Dp = 240.dp // Constant size for the timer
) {
    Box(
        modifier = modifier.size(fixedSize),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasSize = size.minDimension

            // Draw the base circle
            drawArc(
                color = circleColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = Size(canvasSize, canvasSize),
                topLeft = Offset((size.width - canvasSize) / 2, (size.height - canvasSize) / 2)
            )

            // Draw the progress arc
            if (isTimerRunning) {
                drawArc(
                    color = progressColor,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    size = Size(canvasSize, canvasSize),
                    topLeft = Offset((size.width - canvasSize) / 2, (size.height - canvasSize) / 2)
                )
            }
        }

        // Display the remaining time
        Text(
            text = timeLeft.toString(),
            style = MaterialTheme.typography.headlineLarge,
            //color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimerClockPreview() {
    TimerClock(
        progress = 0.75f,
        timeLeft = 45,
        isTimerRunning = true,
        strokeWidth = 12f
    )
}


@Preview(showBackground = true)
@Composable
fun TimerScreenPreview(){
    TimerDetailsScreen(
        timerName = "Timer Name"
    )
}
