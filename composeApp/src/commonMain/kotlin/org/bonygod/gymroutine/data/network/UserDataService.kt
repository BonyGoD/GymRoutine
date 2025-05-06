package org.bonygod.gymroutine.data.network

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json.Default.parseToJsonElement
import kotlinx.serialization.json.jsonObject
import org.bonygod.gymroutine.core.network.NetworkProvider
import org.bonygod.gymroutine.data.mapper.routinesToFirestoreFormat
import org.bonygod.gymroutine.data.model.Routine
import org.bonygod.gymroutine.data.model.UserRequest
import org.bonygod.gymroutine.data.model.UserResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class UserDataService : KoinComponent {

    private val apiKey: String by inject(named("API_KEY"))
    private val client = NetworkProvider().provideHttpClient()

    suspend fun saveUser(user: UserRequest) {

        val response: HttpResponse =
            client.patch("https://firestore.googleapis.com/v1/projects/gym-routine-bonygod/databases/(default)/documents/users/${user.id}?key=$apiKey") {
                contentType(ContentType.Application.Json)
                setBody(user.toFirestoreFormat())
            }
        if (!response.status.isSuccess()) {
            throw Exception("Failed to save user: ${response.status}")
        }
    }

    suspend fun getUser(userId: String): UserResponse {

        val response: HttpResponse = client.get("https://firestore.googleapis.com/v1/projects/gym-routine-bonygod/databases/(default)/documents/users/$userId?key=$apiKey")
        if (!response.status.isSuccess()) {
            throw Exception("Failed to save user: ${response.status}")
        }

        val json = parseToJsonElement(response.body()).jsonObject
        val fields = json["fields"]?.jsonObject

        return UserResponse.fromFirestoreFormat(fields)
    }

    suspend fun addRoutines(userRequest: UserRequest, routines: List<Routine>) {
        val response: HttpResponse =
            client.patch("https://firestore.googleapis.com/v1/projects/gym-routine-bonygod/databases/(default)/documents/users/${userRequest.id}?key=$apiKey") {
                contentType(ContentType.Application.Json)
                setBody(routines.routinesToFirestoreFormat())
            }
        if (!response.status.isSuccess()) {
            throw Exception("Failed to save routine: ${response.status}")
        }
    }
}