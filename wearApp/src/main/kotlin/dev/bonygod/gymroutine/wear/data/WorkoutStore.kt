package dev.bonygod.gymroutine.wear.data

import android.content.Context
import dev.bonygod.gymroutine.wear.model.ActiveWorkout
import kotlinx.serialization.json.Json

private const val PREFS_NAME = "gymroutine_watch"
private const val KEY_ACTIVE_WORKOUT = "active_workout"

class WorkoutStore(context: Context) {

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val format = Json { ignoreUnknownKeys = true }

    fun save(workout: ActiveWorkout?) {
        if (workout == null) {
            prefs.edit().remove(KEY_ACTIVE_WORKOUT).apply()
        } else {
            prefs.edit().putString(KEY_ACTIVE_WORKOUT, format.encodeToString(workout)).apply()
        }
    }

    fun load(today: String): ActiveWorkout? {
        val stored = prefs.getString(KEY_ACTIVE_WORKOUT, null) ?: return null
        val workout = runCatching { format.decodeFromString<ActiveWorkout>(stored) }.getOrNull()
        if (workout == null || workout.startedOn != today) {
            prefs.edit().remove(KEY_ACTIVE_WORKOUT).apply()
            return null
        }
        return workout
    }
}
