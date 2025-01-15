package ke.don.datasource.domain.utils

import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun reformatDuration(time: Int): String {
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


fun reformatDateTime(date: Date): String {
    val currentTime = System.currentTimeMillis()
    val differenceInMillis = currentTime - date.time

    return when {
        differenceInMillis < TimeUnit.MINUTES.toMillis(1) -> "Just now"
        differenceInMillis < TimeUnit.HOURS.toMillis(1) -> {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(differenceInMillis)
            "$minutes minute${if (minutes > 1) "s" else ""} ago"
        }
        differenceInMillis < TimeUnit.DAYS.toMillis(1) -> {
            val hours = TimeUnit.MILLISECONDS.toHours(differenceInMillis)
            "$hours hour${if (hours > 1) "s" else ""} ago"
        }
        differenceInMillis < TimeUnit.DAYS.toMillis(30) -> {
            val days = TimeUnit.MILLISECONDS.toDays(differenceInMillis)
            "$days day${if (days > 1) "s" else ""} ago"
        }
        else -> {
            val months = differenceInMillis / TimeUnit.DAYS.toMillis(30)
            "$months month${if (months > 1) "s" else ""} ago"
        }
    }
}

