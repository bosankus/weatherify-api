package `in`.androidplay.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Feedback(
    val deviceId: String,
    val deviceOs: String,
    val feedbackTitle: String,
    val feedbackDescription: String,
    val timestamp: String = (System.currentTimeMillis() / 1000).toString(),
    @BsonId var id: String = ObjectId().toString()
)
