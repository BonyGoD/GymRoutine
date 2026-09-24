package dev.bonygod.gymroutine.wear.data

import android.content.Context
import android.net.Uri
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.PutDataRequest
import com.google.android.gms.wearable.Wearable
import dev.bonygod.gymroutine.wear.data.mapper.toModel
import dev.bonygod.gymroutine.wear.data.model.WatchRoutinesPayloadDto
import dev.bonygod.gymroutine.wear.data.model.WatchWorkoutResultDto
import dev.bonygod.gymroutine.wear.model.WatchRoutine
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json

private const val PATH_ROUTINES = "/gymroutine/routines"
private const val PATH_REQUEST_ROUTINES = "/gymroutine/request-routines"
private const val PATH_WORKOUT_RESULT = "/gymroutine/workout-result"
private const val KEY_JSON = "json"
private const val KEY_UPDATED_AT = "updatedAt"
private const val CAPABILITY_PHONE = "gymroutine_phone"
private const val WATCH_SCHEMA_VERSION = 1

class PhoneSync(context: Context) {

    private val dataClient = Wearable.getDataClient(context)
    private val messageClient = Wearable.getMessageClient(context)
    private val capabilityClient = Wearable.getCapabilityClient(context)

    private val format = Json { ignoreUnknownKeys = true }

    suspend fun cachedRoutines(): List<WatchRoutine> = runCatching {
        val uri = Uri.Builder().scheme(PutDataRequest.WEAR_URI_SCHEME).path(PATH_ROUTINES).build()
        val buffer = dataClient.getDataItems(uri, DataClient.FILTER_LITERAL).await()
        val payload = try {
            buffer.mapNotNull { item ->
                val dataMap = DataMapItem.fromDataItem(item).dataMap
                val payloadJson = dataMap.getString(KEY_JSON) ?: return@mapNotNull null
                dataMap.getLong(KEY_UPDATED_AT) to payloadJson
            }.maxByOrNull { it.first }?.second
        } finally {
            buffer.release()
        }
        payload?.let { decodeRoutines(it) } ?: emptyList()
    }.getOrElse { emptyList() }

    private fun decodeRoutines(payload: String): List<WatchRoutine> {
        val dto = runCatching { format.decodeFromString<WatchRoutinesPayloadDto>(payload) }.getOrNull() ?: return emptyList()
        if (dto.schemaVersion != WATCH_SCHEMA_VERSION) return emptyList()
        return dto.routines.map { it.toModel() }
    }

    fun routinesUpdates(): Flow<List<WatchRoutine>> = callbackFlow {
        val listener = DataClient.OnDataChangedListener { dataEvents ->
            dataEvents.forEach { event ->
                if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == PATH_ROUTINES) {
                    val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                    val payloadJson = dataMap.getString(KEY_JSON)
                    if (payloadJson != null) {
                        trySend(decodeRoutines(payloadJson))
                    }
                }
            }
        }
        dataClient.addListener(listener).await()
        awaitClose { dataClient.removeListener(listener) }
    }

    suspend fun requestRoutines(): Boolean = runCatching {
        val capability = capabilityClient.getCapability(CAPABILITY_PHONE, CapabilityClient.FILTER_REACHABLE).await()
        val nodes = capability.nodes
        nodes.forEach { node -> messageClient.sendMessage(node.id, PATH_REQUEST_ROUTINES, ByteArray(0)).await() }
        nodes.isNotEmpty()
    }.getOrElse { false }

    suspend fun sendResult(result: WatchWorkoutResultDto) {
        val payload = format.encodeToString(result)
        val request = PutDataMapRequest.create("$PATH_WORKOUT_RESULT/${result.resultId}")
            .apply { dataMap.putString(KEY_JSON, payload) }
            .asPutDataRequest()
            .setUrgent()
        dataClient.putDataItem(request).await()
    }
}
