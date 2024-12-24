package ke.don.timekeeperson.presentation.screens.home

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ke.don.timekeeperson.R
import ke.don.timekeeperson.data.model.Timer

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel<HomeViewModel>()
){
    val allTimers by viewModel.allTimers.collectAsState()

    TimersList(
        timersList = allTimers
    )
}

@Composable
fun TimersList(
    timersList: List<Timer>
){
    LazyColumn{
        items(
            count =timersList.size,
            key = {timer -> timersList[timer].id}){ timer ->
            TimerItem(timer = timersList[timer])
        }

    }
}

@Composable
fun TimerItem(
    timer: Timer
){
    ListItem(
        headlineContent = {
            Text(
                text= timer.name
            )
        },
        supportingContent = {
            Text(
                text = stringResource(R.string.created_on) + timer.dateCreated.toString()
            )
        }
        
    )
}