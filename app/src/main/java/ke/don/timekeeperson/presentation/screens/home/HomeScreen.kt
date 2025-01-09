package ke.don.timekeeperson.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ke.don.timekeeperson.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onTimerClicked : (Int) -> Unit,
    onAddTimer: () -> Unit
){
    val homeUiState by viewModel.homeUiState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddTimer() },
                modifier = modifier.padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_timer),
                    modifier = modifier.padding(8.dp)
                )
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            text = stringResource( R.string.app_name)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        TimersList(
            isTimerListEmpty = homeUiState.timerIsEmpty,
            timersList = homeUiState.allTimers,
            modifier = modifier.padding(innerPadding),
            onTimerClicked = onTimerClicked
        )
    }



}

@Composable
fun TimersList(
    modifier: Modifier = Modifier,
    isTimerListEmpty : Boolean,
    onTimerClicked: (Int) -> Unit,
    timersList: List<TimerUiState>
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ){
        if (isTimerListEmpty){
            EmptyList(
                text = "No timers yet. \nAdd a timer to get started",
                modifier = modifier
                    .align(Alignment.Center)
                    .fillMaxSize()
                    .padding(8.dp)
            )

        }else{
            LazyColumn{
                items(
                    count =timersList.size,
                    key = {timer -> timersList[timer].id}){ timer ->
                    TimerItem(
                        timer = timersList[timer],
                        onTimerClicked = onTimerClicked
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
    timer: TimerUiState,
    onTimerClicked: (Int) -> Unit
){

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onTimerClicked(timer.id)
            }
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start,
            modifier = modifier.padding(8.dp)
        ) {
            Text(
                text = timer.name,
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                modifier = modifier.alpha(0.8f)
            )
            Text(
                text = timer.totalDuration,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = timer.dateCreatedOrLastEdited,
                style = MaterialTheme.typography.bodyMedium,
                modifier = modifier.alpha(0.6f)
            )
        }
    }
}

@Preview
@Composable
fun TimerItemPreview() {
    TimerItem(
        timer = TimerUiState(),
        onTimerClicked = {}

    )
}

@Preview
@Composable
fun TimerListPreview(){
    TimersList(
        isTimerListEmpty = true,
        timersList = emptyList(),
        onTimerClicked = {}
    )
}
