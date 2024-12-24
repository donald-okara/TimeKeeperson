package ke.don.timekeeperson.timer

import androidx.lifecycle.SavedStateHandle
import ke.don.timekeeperson.presentation.screens.timer.TimerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TimerViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: TimerViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Set the Main dispatcher to the test dispatcher
        viewModel = TimerViewModel(
            savedStateHandle = SavedStateHandle()
        ) // Initialize your ViewModel here
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the Main dispatcher
        testDispatcher.cancel() // Clean up the dispatcher
    }

    @Test
    fun `setTimer() updates timer`() = runTest {
        // Given
        val time = 10

        //When
        viewModel.setTimer(time)

        //Then
        assertEquals(time, viewModel.initialTime)
    }




}