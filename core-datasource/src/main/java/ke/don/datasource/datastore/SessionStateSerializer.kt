package ke.don.datasource.datastore
import android.content.Context
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import ke.don.datasource.domain.SessionState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object SessionStateSerializer : Serializer<SessionState> {
    override val defaultValue: SessionState = SessionState()

    override suspend fun readFrom(input: InputStream): SessionState {
        return try {
            Json.decodeFromString(input.readBytes().decodeToString())
        } catch (e: Exception) {
            defaultValue
        }
    }

    override suspend fun writeTo(t: SessionState, output: OutputStream) {
        output.write(Json.encodeToString(t).encodeToByteArray())
    }
}


val Context.sessionStateDataStore by dataStore(
    fileName = "session_state.json",
    serializer = SessionStateSerializer
)
