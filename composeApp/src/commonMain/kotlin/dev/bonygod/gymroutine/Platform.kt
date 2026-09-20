package dev.bonygod.gymroutine

interface Platform {
    val name: String
    val isDebugBuild: Boolean
    val adsEnabled: Boolean
}

expect fun getPlatform(): Platform
