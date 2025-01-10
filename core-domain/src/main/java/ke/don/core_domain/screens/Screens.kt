package ke.don.core_domain.screens

sealed class Screens(
    var route: String
) {
    data object Home: Screens("home")
    data object AddTimer: Screens("add_timer")
    data object TimerDetails: Screens("timer_details/{timerId}"){
        const val timerIdNavigationArgument = "timerId"
    }
}

