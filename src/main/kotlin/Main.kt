import com.mongodb.kotlin.client.MongoClient
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime
import kotlin.random.Random

data class Event(
    @BsonId val id: ObjectId? = null,
    val buildingId: String,
    val eventName: String,
    val data: String? = null,
    val timestamp: String = LocalDateTime.now().toString()
)

data class Measurement(
    @BsonId val id: ObjectId,
    val buildingId: String,
    val brightness: Int,
    val temperature: Int,
    val humidity: Int,
    val gas: Int,
    val timestamp: String = LocalDateTime.now().toString()
)

fun main() {
    val connectionString = "mongodb://root:rootpassword@localhost:27017"
    val databaseName = "smarthome"
    val eventCollectionName = "events"
    val measurementCollectionName = "measurements"
    val buildingIds = listOf("haus-1", "haus-2", "haus-3", "haus-4")

    MongoClient.create(connectionString).use { client ->
        val db = client.getDatabase(databaseName)

        val eventCollection = db.getCollection<Event>(eventCollectionName)
        val measurementCollection = db.getCollection<Measurement>(measurementCollectionName)
        val generator = DataGenerator()

        while (true) {
            val buildingId = buildingIds.random(Random)
            val measurement = generator.nextMeasurement(buildingId)
            val event = generator.nextEvent(buildingId, measurement)

            measurementCollection.insertOne(measurement)
            eventCollection.insertOne(event)

            println("Inserted building=$buildingId measurement=${measurement.id} event=${event.id} at ${measurement.timestamp}")
            Thread.sleep(1_000)
        }
    }
}
