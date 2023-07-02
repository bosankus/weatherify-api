package `in`.androidplay.routes

import `in`.androidplay.db.createOrUpdateFeedback
import `in`.androidplay.data.model.Feedback
import `in`.androidplay.data.model.FeedbackResponse
import `in`.androidplay.db.deleteFeedbackById
import `in`.androidplay.db.getFeedbackById
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.feedbackRoute() {
    route("/") {
        get {
            call.respondText(
                text = "Welcome to the Androidplay API portal.\nUnfortunately, we regret to inform you that there are currently no publicly available APIs at this time. We apologize for any inconvenience caused."
            )
        }
    }

    route("/get-feedback") {
        get {
            val feedbackId = call.request.queryParameters["id"]
            val feedback = feedbackId?.let { id -> getFeedbackById(id = id) }
                ?: call.respond(
                    status = HttpStatusCode.OK,
                    message = FeedbackResponse(
                        status = false,
                        message = "Incorrect or no data provided",
                        data = Unit
                    )
                )

            call.respond(
                status = HttpStatusCode.OK,
                message = FeedbackResponse(
                    status = true,
                    message = "Feedback retrieved successfully",
                    data = feedback
                )
            )
        }
    }

    route("/add-feedback") {
        post {
            val request = try {
                val parameters = call.request.queryParameters
                Feedback(
                    deviceId = parameters["deviceId"].toString(),
                    deviceOs = parameters["deviceOs"].toString(),
                    feedbackTitle = parameters["feedbackTitle"].toString(),
                    feedbackDescription = parameters["feedbackDescription"].toString()
                )
            } catch (e: Exception) {
                e.message
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = FeedbackResponse(
                        status = false,
                        message = HttpStatusCode.BadRequest.description,
                        data = Unit
                    )
                )
                return@post
            }

            if (createOrUpdateFeedback(request)) {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = FeedbackResponse(
                        status = true,
                        message = "Feedback submitted successfully",
                        data = Unit
                    )
                )
            } else {
                call.respond(
                    status = HttpStatusCode.Conflict,
                    message = FeedbackResponse(
                        status = true,
                        message = HttpStatusCode.Conflict.description,
                        data = Unit
                    )
                )
            }
        }
    }

    route("/remove-feedback") {
        delete {
            val feedbackId = call.request.queryParameters["id"]
            val isFeedbackDeleted = feedbackId?.let { id -> deleteFeedbackById(id) } ?: false

            if (isFeedbackDeleted) {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = FeedbackResponse(
                        status = true,
                        message = "Feedback removed successfully",
                        data = Unit
                    )
                )
            } else {
                call.respond(
                    status = HttpStatusCode.OK,
                    message = FeedbackResponse(
                        status = true,
                        message = "Non-existent feedback can't be removed",
                        data = Unit
                    )
                )
            }
        }
    }
}