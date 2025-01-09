package ke.don.timekeeperson.presentation.screens.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ke.don.timekeeperson.presentation.utils.reformatDuration

@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<TimerViewModel>()
    val fraction by viewModel.progress.collectAsState()
    val timeRemaining by viewModel.timeRemaining.collectAsState()

    val timeRemainingFormatted by remember(timeRemaining) {
        derivedStateOf {
            reformatDuration(timeRemaining)
        }
    }

    val timerRunning by viewModel.timerRunning.collectAsState()
    val elapsed by viewModel.elapsed.collectAsState()

    TimerBox(
        modifier = modifier,
        onTimerClicked = {
            viewModel.onTimerClicked()
        },
        onStopClicked = {
            viewModel.stopTimer()
        },
        timeRemainingFormatted = timeRemainingFormatted,
        elapsed = elapsed,
        fraction = fraction,
        timerRunning = timerRunning
    )


}

@Composable
fun TimerBox(
    modifier: Modifier = Modifier,
    onTimerClicked : () -> Unit = {},
    onStopClicked : () -> Unit = {},
    timeRemainingFormatted : String,
    elapsed : Boolean,
    fraction : Float,
    timerRunning : Boolean,
){
    // Access MaterialTheme colors
    val tertiaryContainerColor = if (elapsed) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
    val surfaceColor =  MaterialTheme.colorScheme.surface
    val boxSize = 500.dp

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
                .size(boxSize)
                .padding(16.dp)
                .clip(RoundedCornerShape(40f))
                .drawBehind {
                    val grayWidth = size.width * fraction

                    // Draw the light gray section first (from the left)
                    drawRoundRect(
                        color = tertiaryContainerColor,
                        size = Size(width = size.width - grayWidth, height = size.height),
                        //style = Stroke(width = strokeWidth),

                    )

                    // Draw the gray section (from the right)
                    drawRect(
                        color = surfaceColor,
                        topLeft = Offset(x = size.width - grayWidth, y = 0f),
                        size = Size(width = grayWidth, height = size.height),
                        //style = Stroke(width = strokeWidth)
                    )
                }
        ) {
            StoryCard(
                modifier = modifier
                    .align(Alignment.Center),
                text = timeRemainingFormatted
            )
        }

        ControlPanelRow(
            modifier = modifier,
            onTimerClicked = onTimerClicked,
            onStopClicked = onStopClicked,
            timerRunning = timerRunning
        )


    }
}


@Composable
fun ControlPanelRow(
    modifier: Modifier = Modifier,
    onTimerClicked : () -> Unit = {},
    onStopClicked : () -> Unit = {},
    timerRunning: Boolean
){
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {
                onTimerClicked()
            }
        ) {
            Icon(
                imageVector = if (timerRunning) Icons.Outlined.Pause else Icons.Default.PlayArrow,
                contentDescription = if (timerRunning) "Pause" else "Play"
            )
        }

        IconButton(
            onClick = {
                onStopClicked()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Stop,
                contentDescription = "Stop"
            )
        }
    }
}

@Composable
fun StoryCard(
    modifier: Modifier = Modifier,
    text : String = "Hello",
){
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent, // Color of the card background
            contentColor = MaterialTheme.colorScheme.onSurface  // Color of the content inside the card (e.g., text)
        ),
        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(40f))
            .fillMaxSize()
    ) {
        Box(
            modifier = modifier.fillMaxSize()
        ){
            Text(
                text = text,
                modifier = modifier
                    .align(Alignment.Center) // Align the Text at the center of the Box
                    .padding(16.dp),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun TimerBoxPreview(){
    TimerScreen()
}

