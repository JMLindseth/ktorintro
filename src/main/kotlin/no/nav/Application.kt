package no.nav

import io.ktor.server.application.serverConfig
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import no.nav.plugins.configureRouting
import no.nav.plugins.configureSecurity
import no.nav.plugins.configureSerialization
import no.nav.plugins.configureStatusPages

fun main() {
    val applicationEnvironment =
        applicationEnvironment {}

    val appProperties =
        serverConfig(applicationEnvironment) {
            module {
                configureSecurity()
                configureRouting()
                configureSerialization()
                configureStatusPages()
            }
        }

    embeddedServer(
        Netty,
        appProperties,
    ).start(wait = true)
}
