package `in`.androidplay.data.model

data class FeedbackResponse<T>(
    var status: Boolean, var message: String, var data: T
)
