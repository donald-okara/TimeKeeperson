package ke.don.timekeeperson.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ke.don.timekeeperson.presentation.screens.home.HomeScreen
import ke.don.timekeeperson.presentation.screens.time_picker.AddTimerRoute
import ke.don.timekeeperson.presentation.screens.timer_details.TimerDetailsRoute
import ke.don.timekeeperson.presentation.screens.timer_details.TimerDetailsScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
){
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        composable(Screens.Home.route){
            HomeScreen(
                onTimerClicked = { timerId ->
                    navController.navigate(
                        Screens.TimerDetails.route.replace(
                            oldValue = "{timerId}",
                            newValue = timerId.toString()
                        )
                    )
                },
                onAddTimer = {
                    navController.navigate(Screens.AddTimer.route)
                }
            )
        }

        composable(Screens.AddTimer.route){
            AddTimerRoute(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screens.TimerDetails.route){
            TimerDetailsRoute()
        }
    }

}