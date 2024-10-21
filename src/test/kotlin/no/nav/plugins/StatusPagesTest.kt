package no.nav.plugins

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import junit.framework.TestCase.assertEquals
import kotlin.Exception
import kotlin.test.Test

class StatusPagesTest {
    @Test
    fun `Skal få Conflict fra endepunkt som kaster IllegalArgumentException`() =
        withTestAppMedException(
            path = "/conflict",
            exception = IllegalArgumentException("Feil!"),
        ) {
            client.get("/statuspages/conflict").apply {
                assertEquals(HttpStatusCode.Conflict, status)
                assert(bodyAsText().contains("Ulovlig tilstand: Feil!"))
            }
        }

    @Test
    fun `Skal få status 500 fra endepunkt som kaster RTE`() =
        withTestAppMedException(
            path = "/error",
            exception = RuntimeException("Nei! :("),
        ) {
            client.get("/statuspages/error").apply {
                assertEquals(HttpStatusCode.InternalServerError, status)
                assert(bodyAsText().contains("Noe gikk galt: Nei! :("))
            }
        }

    private fun withTestAppMedException(
        path: String,
        exception: Exception,
        test: suspend ApplicationTestBuilder.() -> Unit,
    ) = testApplication {
        application {
            configureStatusPages()
            routing {
                route("/statuspages") {
                    get(path) {
                        throw exception
                    }
                }
            }
        }
        test()
    }
}
