package `in`.androidplay.db

import `in`.androidplay.data.model.Feedback
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


private val client = KMongo.createClient("YOUR_MONGO_CONNECTION_STRING").coroutine
private val database = client.getDatabase("YOUR_MONGO_DB")
private val feedbacks = database.getCollection<Feedback>()

suspend fun getFeedbackById(id: String): Feedback? {
    return feedbacks.findOneById(id)
}

suspend fun createOrUpdateFeedback(feedback: Feedback): Boolean {
    val feedbackExists = feedbacks.findOneById(feedback.id) != null
    return if (feedbackExists) {
        feedbacks.updateOneById(feedback.id, feedback).wasAcknowledged()
    } else {
        feedback.id = ObjectId().toString()
        feedbacks.insertOne(feedback).wasAcknowledged()
    }
}

suspend fun deleteFeedbackById(id: String): Boolean {
    val feedback = feedbacks.findOneById(id)
    feedback?.let { return feedbacks.deleteOneById(id).wasAcknowledged() } ?: return false
}
