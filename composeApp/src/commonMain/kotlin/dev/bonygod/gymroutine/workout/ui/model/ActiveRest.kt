package dev.bonygod.gymroutine.workout.ui.model

private const val MILLIS_PER_SECOND = 1_000L

data class ActiveRest(
    val endsAtMillis: Long,
    val secondsLeft: Int,
) {
    fun isFinished(nowMillis: Long): Boolean = nowMillis >= endsAtMillis

    fun tick(nowMillis: Long) = copy(
        secondsLeft = ((endsAtMillis - nowMillis + MILLIS_PER_SECOND - 1) / MILLIS_PER_SECOND)
            .toInt()
            .coerceAtLeast(0),
    )

    companion object {
        fun start(nowMillis: Long, restSeconds: Int) = ActiveRest(
            endsAtMillis = nowMillis + restSeconds * MILLIS_PER_SECOND,
            secondsLeft = restSeconds,
        )
    }
}
