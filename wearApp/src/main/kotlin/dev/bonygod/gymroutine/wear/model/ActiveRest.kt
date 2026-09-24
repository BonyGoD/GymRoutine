package dev.bonygod.gymroutine.wear.model

private const val MILLIS_PER_SECOND = 1_000L

data class ActiveRest(
    val exerciseIndex: Int,
    val endsAtMillis: Long,
    val totalSeconds: Int,
    val secondsLeft: Int,
) {
    fun isFinished(nowMillis: Long): Boolean = nowMillis >= endsAtMillis

    fun tick(nowMillis: Long) = copy(
        secondsLeft = ((endsAtMillis - nowMillis + MILLIS_PER_SECOND - 1) / MILLIS_PER_SECOND)
            .toInt()
            .coerceAtLeast(0),
    )

    companion object {
        fun start(exerciseIndex: Int, nowMillis: Long, restSeconds: Int) = ActiveRest(
            exerciseIndex = exerciseIndex,
            endsAtMillis = nowMillis + restSeconds * MILLIS_PER_SECOND,
            totalSeconds = restSeconds,
            secondsLeft = restSeconds,
        )
    }
}
