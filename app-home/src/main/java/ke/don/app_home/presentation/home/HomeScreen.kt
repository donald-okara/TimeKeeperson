package ke.don.app_home.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ke.don.app_home.R
import ke.don.app_home.domain.states.TimerUiState
import ke.don.datasource.domain.SessionState
import ke.don.datasource.domain.model.SessionStatus
import ke.don.feature_timer.presentation.screens.timer_session.TimerDetailsScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    runningColor: Color,
    pausedColor: Color,
    completedColor: Color,
    onAddTimer: () -> Unit
) {
    val homeUiState by viewModel.homeUiState.collectAsState()

    val sessionStatus = homeUiState.sessionState.status
    val sessionState = homeUiState.sessionState

    val selectedTimerId by viewModel.selectedTimerId.collectAsState()

    // Transition for session state changes (idle vs active)
    val transition = updateTransition(targetState = sessionState.status != SessionStatus.IDLE, label = "Session State Transition")

    // Scale transition for the timer details screen when switching between idle and active state
    val scale by transition.animateFloat(
        label = "Scale Transition",
        transitionSpec = {
            if (targetState) {
                // Scale up when session is active
                tween(durationMillis = 300)
            } else {
                // Scale down when session is idle
                tween(durationMillis = 300)
            }
        }
    ) { state ->
        if (state) 1.1f else 1f
    }

    // Alpha transition for smooth visibility change
    val alpha by transition.animateFloat(
        label = "Alpha Transition",
        transitionSpec = {
            if (targetState) {
                tween(durationMillis = 300)
            } else {
                tween(durationMillis = 300)
            }
        }
    ) { state ->
        if (state) 1f else 0.6f
    }

    Scaffold(
        floatingActionButton = {
            if (sessionState.status == SessionStatus.IDLE) {
                FloatingActionButton(
                    onClick = { onAddTimer() },
                    modifier = modifier.padding(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.add_timer),
                        modifier = modifier.padding(8.dp)
                    )
                }
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.app_name)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (sessionState.status != SessionStatus.IDLE) {
                // TimerDetailsScreen when session is active
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(durationMillis = 300)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300))
                ) {
                    TimerDetailsScreen(
                        status = sessionState.status,
                        progress = 1f,
                        timeLeft = sessionState.timeLeft,
                        timerName = sessionState.timerName,
                        onPauseTimer = viewModel::onPauseTimer,
                        onResumeTimer = viewModel::onResumeTimer,
                        onStartTimer = viewModel::onStartTimer,
                        onCancel = viewModel::onCancel,
                        onStopTimer = viewModel::onStopTimer,
                        onSaveSession = viewModel::onSaveSession,
                        runningColor = runningColor,
                        pausedColor = pausedColor,
                        completedColor = completedColor,
                        modifier = modifier
                            .padding(8.dp)
                            .graphicsLayer(scaleX = scale, scaleY = scale)
                    )
                }
            } else {
                // TimersList when session is idle
                TimersList(
                    isTimerListEmpty = homeUiState.timerIsEmpty,
                    timersList = homeUiState.allTimers,
                    sessionState = sessionState,
                    onTimerClicked = { timer ->
                        viewModel.onSelectTimer(timer)
                    },
                    selectedTimerId = selectedTimerId,
                    modifier = modifier,
                    sessionStatus = sessionStatus,
                    runningColor = runningColor,
                    pausedColor = pausedColor,
                    completedColor = completedColor,
                    onPauseTimer = viewModel::onPauseTimer,
                    onResumeTimer = viewModel::onResumeTimer,
                    onStartTimer = viewModel::onStartTimer,
                    onCancel = viewModel::onCancel,
                    onStopTimer = viewModel::onStopTimer,
                    onSaveSession = viewModel::onSaveSession
                )
            }
        }
    }
}


@Composable
fun TimersList(
    modifier: Modifier = Modifier,
    isTimerListEmpty : Boolean,
    sessionState: SessionState = SessionState(),
    onTimerClicked: (TimerUiState) -> Unit,
    sessionStatus: SessionStatus,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
    onStartTimer: () -> Unit,
    onCancel: () -> Unit,
    onStopTimer: () -> Unit,
    onSaveSession: () -> Unit,
    runningColor: Color,
    pausedColor: Color,
    completedColor: Color,
    selectedTimerId : Int? = null,
    timersList: List<TimerUiState>
){
    Box(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ){
        if (isTimerListEmpty){
            EmptyList(
                text = "No timers yet. \nAdd a timer to get started",
                modifier = modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .padding(8.dp)
            )

        } else {
            LazyColumn {
                items(
                    count = timersList.size,
                    key = { timer -> timersList[timer].id }) { timer ->
                    TimerItem(
                        isSelected = selectedTimerId == timersList[timer].id,
                        sessionState = sessionState,
                        timer = timersList[timer],
                        onTimerClicked = onTimerClicked,
                        runningColor = runningColor,
                        pausedColor = pausedColor,
                        completedColor = completedColor,
                        onPauseTimer = onPauseTimer,
                        onResumeTimer = onResumeTimer,
                        onStartTimer = onStartTimer,
                        onCancel = onCancel,
                        onStopTimer = onStopTimer,
                        onSaveSession = onSaveSession
                    )
                }
            }

        }
    }
}

@Composable
fun EmptyList(
    modifier: Modifier = Modifier,
    text: String
){
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}


@Composable
fun TimerItem(
    modifier: Modifier = Modifier,
    runningColor: Color,
    pausedColor: Color,
    completedColor: Color,
    onPauseTimer: () -> Unit,
    onResumeTimer: () -> Unit,
    onStartTimer: () -> Unit,
    onCancel: () -> Unit,
    onStopTimer: () -> Unit,
    onSaveSession: () -> Unit,
    timer: TimerUiState,
    sessionState: SessionState = SessionState(),
    isSelected: Boolean = false,
    onTimerClicked: (TimerUiState) -> Unit,
) {
    // Create a transition for the selection state
    val transition = updateTransition(targetState = isSelected, label = "Selection Transition")

    // Define the look-ahead transition
    val scale by transition.animateFloat(
        label = "Scale Transition",
        transitionSpec = {
            if (targetState) {
                // Scale up when selected
                tween(durationMillis = 300)
            } else {
                // Scale down when unselected
                tween(durationMillis = 300)
            }
        }
    ) { state ->
        if (state) 1.1f else 1f
    }

    val alpha by transition.animateFloat(
        label = "Alpha Transition",
        transitionSpec = {
            if (targetState) {
                tween(durationMillis = 300)
            } else {
                tween(durationMillis = 300)
            }
        }
    ) { state ->
        if (state) 1f else 0.6f
    }

    // If session state is not idle, fill the entire screen with the selected timer item
    // Regular card view for the timer item when not selected or when session is idle
    if (sessionState.status == SessionStatus.IDLE) {
        AnimatedVisibility(visible = isSelected) {
            TimerDetailsScreen(
                status = sessionState.status,
                progress = 1f,
                timeLeft = sessionState.timeLeft,
                timerName = timer.name,
                onPauseTimer = onPauseTimer,
                onResumeTimer = onResumeTimer,
                onStartTimer = onStartTimer,
                onCancel = onCancel,
                onStopTimer = onStopTimer,
                onSaveSession = onSaveSession,
                runningColor = runningColor,
                pausedColor = pausedColor,
                completedColor = completedColor,
                modifier = modifier
                    .clickable {
                        onTimerClicked(timer)
                    }
                    .padding(8.dp)
            )
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    onTimerClicked(timer)
                }
                .animateContentSize() // Smoothly animates size changes
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale
                )
        ) {
            AnimatedVisibility(visible = !isSelected) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = timer.name,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        modifier = Modifier.alpha(alpha)
                    )
                    Text(
                        text = timer.totalDurationReformated,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = timer.dateCreatedOrLastEdited,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.alpha(0.6f)
                    )
                }
            }

        }
    }

}




