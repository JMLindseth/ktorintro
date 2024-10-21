package no.nav.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.jwt.JWTAuthenticationProvider
import io.ktor.server.auth.jwt.JWTPrincipal
import no.nav.plugins.jwtAudience
import no.nav.plugins.jwtDomain
import no.nav.plugins.jwtRealm
import no.nav.plugins.jwtSecret

fun JWTAuthenticationProvider.Config.hjemmelagd() {
    realm = jwtRealm
    verifier(
        JWT
            .require(Algorithm.HMAC256(jwtSecret))
            .withAudience(jwtAudience)
            .withIssuer(jwtDomain)
            .build(),
    )
    validate { credential ->
        if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
    }
}
