package org.bonygod.gymroutine.data.network

import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import org.bonygod.gymroutine.core.network.NetworkProvider
import org.bonygod.gymroutine.data.model.UserData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class UserDataService : KoinComponent {

    private val apiKey: String by inject(named("API_KEY"))
    private val client = NetworkProvider().provideHttpClient()

    suspend fun saveUser(user: UserData) {

        val response: HttpResponse =
            client.patch("https://firestore.googleapis.com/v1/projects/gym-routine-bonygod/databases/(default)/documents/users/${user.id}?key=$apiKey") {
                contentType(ContentType.Application.Json)
                setBody(user.toFirestoreFormat())
            }
        if (!response.status.isSuccess()) {
            throw Exception("Failed to save user: ${response.status}")
        }
    }
}