package `in`.androidplay.routes

import `in`.androidplay.db.createOrUpdateFeedback
import `in`.androidplay.data.model.Feedback
import `in`.androidplay.data.model.FeedbackResponse
import `in`.androidplay.data.model.RemoveFeedbackRequest
import `in`.androidplay.data.requests.FeedbackRequest
import `in`.androidplay.db.deleteFeedbackById
import `in`.androidplay.db.getFeedbackById
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.feedbackRoute() {
    route("/get-feedback") {
        get {
            val feedbackId = call.receive<FeedbackRequest>().id
            val feedback = getFeedbackById(id = feedbackId)
            feedback?.let { feedbackItem ->
                call.respond(
                    status = HttpStatusCode.OK,
                    message = FeedbackResponse(
                        status = true,
                        message = "Feedback retrieved successfully",
                        data = feedbackItem
                    )
                )
            } ?: call.respond(
                status = HttpStatusCode.OK,
                message = FeedbackResponse(
                    status = true,
                    message = "No feedback found",
                    data = Unit
                )
            )
        }
    }

    route("/add-feedback") {
        post {
            val request = try {
                call.receive<Feedback>()
            } catch (e: ContentTransformationException) {
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
            val request = try {
                call.receive<RemoveFeedbackRequest>()
            } catch (e: ContentTransformationException) {
                call.respond(
                    status = HttpStatusCode.BadRequest,
                    message = FeedbackResponse(
                        status = false,
                        message = HttpStatusCode.BadRequest.description,
                        data = Unit
                    )
                )
                return@delete
            }

            if (deleteFeedbackById(request.id)) {
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