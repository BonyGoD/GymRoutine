package org.bonygod.gymroutine.data.network

import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.bonygod.gymroutine.core.network.NetworkProvider
import org.bonygod.gymroutine.data.model.AuthResult
import org.bonygod.gymroutine.data.model.FirebaseLoginRequest
import org.bonygod.gymroutine.data.model.FirebaseSignUpRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class AuthenticationService() : KoinComponent {

    private val apiKey: String by inject(named("API_KEY"))
    private val client = NetworkProvider().provideHttpClient()

    suspend fun login(email: String, password: String): AuthResult {
        val response: HttpResponse = client.post("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=$apiKey") {
            contentType(ContentType.Application.Json)
            setBody(FirebaseLoginRequest(email, password))
        }

        val result: AuthResult = response.body()
        return AuthResult(result.idToken, result.email, result.displayName, result.error)
    }

    suspend fun signUp(email: String, password: String, displayName: String): AuthResult {
        val result: AuthResult = client.post("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=$apiKey") {
            contentType(ContentType.Application.Json)
            setBody(FirebaseSignUpRequest(email, password, displayName))
        }.body()

        return AuthResult(result.idToken, result.email, result.displayName, result.error)
    }
}