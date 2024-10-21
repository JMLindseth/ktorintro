package no.nav.plugins

import io.ktor.server.application.Application

val jwtAudience = "ktorintro"
val jwtDomain = "megselv.no"
val jwtRealm = "ktorintro"
val jwtSecret = "secret"

fun Application.configureSecurity() {
}
