package ke.don.timekeeperson.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ke.don.timekeeperson.presentation.screens.home.HomeScreen

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
){
    NavHost(
        navController = navController,
        startDestination = Screens.Home.name
    ) {
        composable(Screens.Home.name){
            HomeScreen()
        }
    }
}