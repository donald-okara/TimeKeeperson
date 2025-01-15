package ke.don.feature_timer.presentation.screens.timer_session

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ke.don.datasource.domain.model.SessionStatus
import ke.don.datasource.domain.utils.reformatDuration
import ke.don.feature_timer.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerDetailsRoute(
    modifier: Modifier = Modifier,
    onNavigateBack : () -> Unit,
    runningColor: Color,
    pausedColor: Color,
    completedColor: Color,
    viewModel: TimerSessionViewModel = hiltViewModel(),
){
    val sessionState by viewModel.sessionState.collectAsState()
    val progress = remember {
        derivedStateOf {
            1f-(sessionState.timeLeft.toFloat() / sessionState.expectedRunTime.toFloat())
        }
    }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = ""
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigateBack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        }
    ) {innerPadding->
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TimerDetailsScreen(
                timerName = sessionState.timerName,
                status = sessionState.status,
                timeLeft = sessionState.timeLeft,
                progress = progress.value,
                onPauseTimer = viewModel::pauseSession,
                onResumeTimer = viewModel::resumeSession,
                onStartTimer = viewModel::startSession,
                onCancel = viewModel::cancel,
                onStopTimer = viewModel::stopSession,
                onSaveSession = viewModel::saveSession,
                runningColor = runningColor,
                pausedColor = pausedColor,
                completedColor = completedColor,
                modifier = modifier
                    .padding(8.dp)
                    .align(Alignment.Center)
            )
        }


    }

}

@Composable
fun TimerDetailsScreen(
    modifier: Modifier = Modifier,
    status: SessionStatus = SessionStatus.IDLE,
    progress: Float = 0f,
    timeLeft: Long,
    timerName: String,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
    onStartTimer: () -> Unit,
    onCancel: () -> Unit,
    onStopTimer: () -> Unit,
    onSaveSession: () -> Unit,
    runningColor: Color,
    pausedColor: Color,
    completedColor: Color,
){
    val spacerWeight = 1f

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

        Spacer(modifier = modifier.weight(spacerWeight))

        TimerClock(
            progress = progress,
            progressColor = when(status){
                SessionStatus.RUNNING-> runningColor
                SessionStatus.PAUSED-> pausedColor
                SessionStatus.COMPLETED-> completedColor
                else -> Color.Transparent
            },
            timeLeft = timeLeft
        )

        Spacer(modifier = modifier.weight(spacerWeight))

        TimerActionsComponent(
            status = status,
            onPauseTimer = onPauseTimer,
            onResumeTimer = onResumeTimer,
            onStartTimer = onStartTimer,
            onCancel = onCancel,
            onStopTimer = onStopTimer,
            onSaveSession = onSaveSession,
            runningColor = runningColor,
            pausedColor = pausedColor,
            completedColor = completedColor
        )
    }

}

@Composable
fun TimerClock(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    timeLeft: Long = 0L,
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
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                size = Size(canvasSize, canvasSize),
                topLeft = Offset(
                    (size.width - canvasSize) / 2,
                    (size.height - canvasSize) / 2
                )
            )


        }

        // Display the remaining time
        Text(
            text = reformatDuration(timeLeft.toInt()),
            style = MaterialTheme.typography.headlineLarge,
            //color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun TimerActionsComponent(
    modifier: Modifier = Modifier,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
    onStartTimer: () -> Unit,
    onCancel: () -> Unit,
    onStopTimer: () -> Unit,
    onSaveSession: () -> Unit,
    status: SessionStatus,
    runningColor: Color,
    pausedColor: Color,
    completedColor: Color,
) {
    AnimatedContent(targetState = status, transitionSpec = {
        fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
    }) { targetStatus ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = modifier.padding(8.dp)
        ) {
            when (targetStatus) {
                SessionStatus.RUNNING -> {
                    ActionButton(text = stringResource(R.string.stop_timer), onClick = onStopTimer)
                    ActionButton(
                        text = stringResource(R.string.pause_timer),
                        onClick = onPauseTimer,
                        isPrimary = true,
                        containerColor = runningColor
                    )
                }

                SessionStatus.PAUSED -> {
                    ActionButton(text = stringResource(R.string.stop_timer), onClick = onStopTimer)
                    ActionButton(
                        text = stringResource(R.string.resume_timer),
                        onClick = onResumeTimer,
                        isPrimary = true,
                        containerColor = pausedColor
                    )
                }

                SessionStatus.COMPLETED -> {
                    ActionButton(text = stringResource(R.string.cancel_timer), onClick = onCancel)
                    ActionButton(
                        text = stringResource(R.string.save_session),
                        onClick = onSaveSession,
                        isPrimary = true,
                        containerColor = completedColor
                    )
                }

                SessionStatus.IDLE -> {
                    ActionButton(
                        text = stringResource(R.string.start_timer),
                        onClick = onStartTimer,
                        isPrimary = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    isPrimary: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier,
    buttonHeight: Dp = 48.dp,
    buttonWidth: Dp = 120.dp
) {
    if (isPrimary) {
        Button(
            onClick = onClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = containerColor // Background color of the button
            ),
            modifier = modifier
                .padding(8.dp)
                .size(width = buttonWidth, height = buttonHeight)
        ) {
            Text(text = text)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier
                .padding(8.dp)
                .size(width = buttonWidth, height = buttonHeight)
        ) {
            Text(text = text)
        }
    }
}



