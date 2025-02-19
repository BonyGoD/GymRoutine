package org.bonygod.gymroutine

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform