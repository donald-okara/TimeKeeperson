package ke.don.app_navigation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ke.don.app_home.presentation.home.HomeScreen
import ke.don.core_domain.screens.Screens
import ke.don.feature_timer.presentation.screens.time_picker.AddTimerRoute
import ke.don.feature_timer.presentation.screens.timer_session.TimerDetailsRoute

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
){
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route,
        modifier = modifier
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
            TimerDetailsRoute(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }

}