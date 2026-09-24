package dev.bonygod.gymroutine.watch

import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

private const val LISTENER_TIMEOUT_MILLIS = 20_000L

class WatchListenerService : WearableListenerService() {

    private val wearSync by lazy { WearSync(applicationContext) }

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(applicationContext)
    }

    override fun onMessageReceived(event: MessageEvent) {
        if (event.path == PATH_REQUEST_ROUTINES) {
            runBlocking { withTimeoutOrNull(LISTENER_TIMEOUT_MILLIS) { wearSync.publishRoutines() } }
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        val hasWorkoutResult = dataEvents.any {
            it.type == DataEvent.TYPE_CHANGED && it.dataItem.uri.path?.startsWith(PATH_WORKOUT_RESULT) == true
        }
        if (hasWorkoutResult) {
            runBlocking { withTimeoutOrNull(LISTENER_TIMEOUT_MILLIS) { wearSync.drainResults() } }
        }
    }
}
