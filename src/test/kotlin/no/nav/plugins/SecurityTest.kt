package no.nav.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import junit.framework.TestCase.assertEquals
import java.util.Date
import kotlin.test.Test

class SecurityTest {
    @Test
    fun `Skal få 401 fra endepunkt som krever autentisering`() =
        withTestApp {
            client.get("/security").apply {
                assertEquals(HttpStatusCode.Unauthorized, status)
            }
        }

    @Test
    fun `Skal få 200 fra endepunkt hvis token sendes med`() =
        withTestApp {
            client
                .get("/security") {
                    bearerAuth(token)
                }.apply {
                    assertEquals(HttpStatusCode.OK, status)
                }
        }

    private fun withTestApp(test: suspend ApplicationTestBuilder.() -> Unit) =
        testApplication {
            application {
                configureSecurity()
                configureRouting()
            }
            test()
        }
}

val token: String =
    JWT
        .create()
        .withAudience("ktorintro")
        .withIssuer("megselv.no")
        .withExpiresAt(Date(System.currentTimeMillis() + 60000))
        .sign(Algorithm.HMAC256("secret"))
