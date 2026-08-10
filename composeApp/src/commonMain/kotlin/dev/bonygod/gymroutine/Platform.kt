package dev.bonygod.gymroutine

interface Platform {
    val name: String
    val isDebugBuild: Boolean
}

expect fun getPlatform(): Platform
