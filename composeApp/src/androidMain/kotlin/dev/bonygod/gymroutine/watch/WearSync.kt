package dev.bonygod.gymroutine.watch

import android.content.Context
import android.net.Uri
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.PutDataRequest
import com.google.android.gms.wearable.Wearable
import dev.bonygod.gymroutine.auth.domain.usecase.GetCurrentUserUseCase
import dev.bonygod.gymroutine.routines.domain.usecase.GetRoutinesUseCase
import dev.bonygod.gymroutine.watch.data.WatchSyncCodec
import dev.bonygod.gymroutine.watch.domain.usecase.ApplyWatchWorkoutUseCase
import dev.bonygod.gymroutine.workout.domain.usecase.ObserveWorkoutLogsUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Clock

private const val PATH_ROUTINES = "/gymroutine/routines"
internal const val PATH_REQUEST_ROUTINES = "/gymroutine/request-routines"
internal const val PATH_WORKOUT_RESULT = "/gymroutine/workout-result"
private const val KEY_JSON = "json"
private const val KEY_UPDATED_AT = "updatedAt"

class WearSync(context: Context) : KoinComponent {

    private val getCurrentUser: GetCurrentUserUseCase by inject()
    private val getRoutines: GetRoutinesUseCase by inject()
    private val applyWatchWorkout: ApplyWatchWorkoutUseCase by inject()
    private val observeWorkoutLogs: ObserveWorkoutLogsUseCase by inject()

    private val dataClient: DataClient = Wearable.getDataClient(context)

    suspend fun publishRoutines() {
        runCatching {
            val uid = getCurrentUser().getOrNull()?.uid ?: return@runCatching
            val routines = getRoutines(uid).getOrNull() ?: return@runCatching
            val logs = observeWorkoutLogs(uid).first()
            val now = Clock.System.now().toEpochMilliseconds()
            val json = WatchSyncCodec.encodeRoutines(routines, logs, now)
            val request = PutDataMapRequest.create(PATH_ROUTINES)
                .apply {
                    dataMap.putString(KEY_JSON, json)
                    dataMap.putLong(KEY_UPDATED_AT, now)
                }
                .asPutDataRequest()
                .setUrgent()
            dataClient.putDataItem(request).await()
        }
    }

    suspend fun drainResults() {
        runCatching {
            val uri = Uri.Builder().scheme(PutDataRequest.WEAR_URI_SCHEME).path(PATH_WORKOUT_RESULT).build()
            val buffer = dataClient.getDataItems(uri, DataClient.FILTER_PREFIX).await()
            val items = try {
                buffer.map { it.uri to DataMapItem.fromDataItem(it).dataMap.getString(KEY_JSON) }
            } finally {
                buffer.release()
            }
            if (items.isEmpty()) return@runCatching
            val uid = getCurrentUser().getOrNull()?.uid ?: return@runCatching
            var appliedAny = false
            items.forEach { (itemUri, json) ->
                val result = json?.let { WatchSyncCodec.decodeResult(it) }
                if (result == null) {
                    dataClient.deleteDataItems(itemUri).await()
                } else if (applyWatchWorkout(uid, result).isSuccess) {
                    dataClient.deleteDataItems(itemUri).await()
                    appliedAny = true
                }
            }
            if (appliedAny) publishRoutines()
        }
    }
}
