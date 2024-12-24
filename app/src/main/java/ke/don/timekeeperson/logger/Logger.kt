package ke.don.timekeeperson.logger

interface Logger {
    fun logDebug(tag: String, message: String)

    fun logError(tag: String, message: String)
}