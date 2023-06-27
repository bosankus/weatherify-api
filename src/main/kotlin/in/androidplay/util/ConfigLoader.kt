package `in`.androidplay.util

import java.lang.IllegalStateException
import java.util.Properties

object ConfigLoader {

    fun loadConfig(): Properties {
        val config = Properties()
        val mongoDBClient: String = System.getenv("MONGO_DB_CLIENT")
            ?: throw IllegalStateException("MongoDB client not found")
        config.setProperty("MONGO_DB_CLIENT", mongoDBClient)
        return config
    }
}