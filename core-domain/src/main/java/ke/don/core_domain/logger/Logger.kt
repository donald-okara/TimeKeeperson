package ke.don.core_domain.logger

interface Logger {
    fun logDebug(tag: String, message: String)

    fun logError(tag: String, message: String)
}