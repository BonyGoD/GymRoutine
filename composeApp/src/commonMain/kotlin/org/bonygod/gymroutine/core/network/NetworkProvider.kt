package org.bonygod.gymroutine.core.network

import io.ktor.client.HttpClient

class NetworkProvider {
    fun provideHttpClient(): HttpClient = NetworkUtils.httpClient
}