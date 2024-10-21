package no.nav.plugins

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import junit.framework.TestCase.assertEquals
import kotlin.test.Test

class RoutingTest {
    @Test
    fun `Skal få 200 OK fra GET-kall til root-endepunkt`() =
        withTestApp {
            client.get("/").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
        }

    @Test
    fun `Skal få 200 OK fra POST-kall til rot-endepunktet`() =
        withTestApp {
            client.post("/").apply {
                assertEquals(HttpStatusCode.OK, status)
            }
        }

    @Test
    fun `Skal få Hello world fra get-endepunkt`() =
        withTestApp {
            client.get("/hello").apply {
                assertEquals(HttpStatusCode.OK, status)
                assertEquals("Hello world", bodyAsText())
            }
        }

    @Test
    fun `Skal få 500 fra endepunkt som feiler`() =
        withTestApp {
            client.get("/error").apply {
                assertEquals(HttpStatusCode.InternalServerError, status)
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
