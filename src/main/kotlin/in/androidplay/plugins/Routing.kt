package `in`.androidplay.plugins

import `in`.androidplay.routes.feedbackRoute
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        feedbackRoute()
    }
}
