package no.nav.plugins

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import junit.framework.TestCase.assertEquals
import no.nav.domain.Objekt
import kotlin.test.Test

class SerializationTest {
    @Test
    fun `Skal få OK ved å sende inn et objekt`() =
        withTestApp {
            val objekt = Objekt("streng", 1)
            client
                .post("/serialization") {
                    contentType(ContentType.Application.Json)
                    setBody(ObjectMapper().writeValueAsString(objekt))
                }.apply {
                    assertEquals(HttpStatusCode.OK, status)
                }
        }

    @Test
    fun `Skal få et objekt tilbake`() =
        withTestApp {
            client.get("/serialization").apply {
                val forventetObjekt =
                    Objekt(
                        felt1 = "Felt",
                        felt2 = 1,
                    )

                assertEquals(forventetObjekt, objectMapper.readValue(bodyAsText(), Objekt::class.java))
            }
        }

    @Test
    fun `Skal motta et objekt med LocalDateTime`() =
        withTestApp {
            client.get("/serialization/dato").apply {
                // language=json
                val forventetJson = """{"dato":"2024-01-01"}"""

                assertEquals(forventetJson, bodyAsText())
            }
        }

    private fun withTestApp(test: suspend ApplicationTestBuilder.() -> Unit) =
        testApplication {
            application {
                configureSecurity()
                configureSerialization()
                configureRouting()
            }
            test()
        }
}

val objectMapper =
    ObjectMapper().apply {
        registerModules(
            KotlinModule
                .Builder()
                .withReflectionCacheSize(512)
                .build(),
        )
    }
