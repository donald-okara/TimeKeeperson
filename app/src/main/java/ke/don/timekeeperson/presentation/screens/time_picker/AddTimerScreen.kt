package ke.don.timekeeperson.presentation.screens.time_picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ke.don.timekeeperson.R


@Composable
fun AddTimerRoute(
    modifier: Modifier = Modifier,
    viewModel: AddTimerViewModel = hiltViewModel<AddTimerViewModel>()
){
    val name by viewModel.name.collectAsState()
    val hours by viewModel.hours.collectAsState()
    val minutes by viewModel.minutes.collectAsState()
    val seconds by viewModel.seconds.collectAsState()
    val totalDuration by viewModel.totalDuration.collectAsState()
    val timer by viewModel.timer.collectAsState()
    val nameError by viewModel.nameError.collectAsState()
    val timerIsValid = viewModel.timerIsValid


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ){
        TimePickerScreen(
            name = name,
            hours = hours,
            minutes = minutes,
            seconds = seconds,
            enabled = timerIsValid,
            totalDuration = totalDuration,
            nameError = nameError,
            onNameValueChange = {viewModel.onNameValueChange(it)},
            onHourValueChange = {viewModel.onHourValueChange(it)},
            onMinuteValueChange = {viewModel.onMinuteValueChange(it)},
            onSecondValueChange = {viewModel.onSecondValueChange(it)},
            onAddTimerClicked = {viewModel.addTimer()}
        )
    }

}

@Composable
fun TimePickerScreen(
    modifier: Modifier = Modifier,
    name: String,
    hours: Int,
    minutes: Int,
    seconds: Int,
    enabled : Boolean,
    totalDuration: Int,
    nameError: String?,
    onNameValueChange: (String) -> Unit,
    onHourValueChange: (Int) -> Unit,
    onMinuteValueChange: (Int) -> Unit,
    onSecondValueChange: (Int) -> Unit,
    onAddTimerClicked: () -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(8.dp)
    ) {
        TextField(
            value = name,
            onValueChange = {onNameValueChange(it)},
            placeholder = {
                Text(
                    text= "Timer name"
                )
            }
        )

        nameError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        TimePickerRow(
            hours = hours,
            minutes = minutes,
            seconds = seconds,
            onHourValueChange = {onHourValueChange(it)},
            onMinuteValueChange = {onMinuteValueChange(it)},
            onSecondValueChange = {onSecondValueChange(it)},
        )

        Button(
            onClick = onAddTimerClicked,
            enabled = enabled
        ){
            Text(
                text = stringResource(R.string.add_timer)
            )
        }
    }
}

@Composable
fun TimePickerRow(
    modifier: Modifier = Modifier,
    hours: Int,
    minutes: Int,
    seconds: Int,
    onHourValueChange: (Int) -> Unit,
    onMinuteValueChange: (Int) -> Unit,
    onSecondValueChange: (Int) -> Unit
){
    val textFieldWidth = 128.dp
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth()
    ){
        OutlinedTextField(
            value = hours.toString(),
            onValueChange = { input ->
                // Ensure the input is a valid number and within the range 0-24
                val numericInput = input.toIntOrNull()
                if (numericInput != null && numericInput in 0..24) {
                    onHourValueChange(numericInput)
                }
            },
            label = { Text("Hours") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = modifier.width(textFieldWidth)

        )

        Text(
            text = ":",
            modifier = modifier.padding(horizontal = 8.dp)
        )
        OutlinedTextField(
            value = minutes.toString(),
            onValueChange = { input ->
                // Ensure the input is a valid number and within the range 0-24
                val numericInput = input.toIntOrNull()
                if (numericInput != null && numericInput in 0..59) {
                    onMinuteValueChange(numericInput)
                }
            },
            label = { Text("Minutes") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = modifier.width(textFieldWidth)
        )

        Text(
            text = ":",
            modifier = modifier.padding(horizontal = 8.dp)
        )
        OutlinedTextField(
            value = seconds.toString(),
            onValueChange = { input ->
                // Ensure the input is a valid number and within the range 0-24
                val numericInput = input.toIntOrNull()
                if (numericInput != null && numericInput in 0..59) {
                    onSecondValueChange(numericInput)
                }
            },
            label = { Text("Seconds") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            modifier = modifier.width(textFieldWidth)

        )
    }
}

@Preview
@Composable
fun TimerPickerRowPreview(){
    TimePickerRow(
        hours = 0,
        minutes = 0,
        seconds = 0,
        onHourValueChange = {},
        onMinuteValueChange = {},
        onSecondValueChange = {}
    )
}