package ke.don.timekeeperson.presentation.utils

import java.util.Locale

fun reformatTime(time: Int): String {
    val hours = time / 3600
    val minutes = (time % 3600) / 60
    val seconds = time % 60

    if (hours>0){
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)

    }else if (minutes > 0){
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    }else {
        return String.format(Locale.getDefault(), "%02d", seconds)
    }
}
