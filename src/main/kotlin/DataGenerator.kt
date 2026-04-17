import org.bson.types.ObjectId
import kotlin.math.roundToInt

class DataGenerator {
    private val brightnessNoise = SmoothNoise()
    private val temperatureNoise = SmoothNoise()
    private val humidityNoise = SmoothNoise()
    private val gasNoise = SmoothNoise()
    private val eventNoise = SmoothNoise()
    private var tick = 0

    fun nextMeasurement(buildingId: String): Measurement {
        tick += 1
        return Measurement(
            id = ObjectId(),
            buildingId = buildingId,
            brightness = scaledValue(brightnessNoise, 250, 950),
            temperature = scaledValue(temperatureNoise, 16, 28),
            humidity = scaledValue(humidityNoise, 35, 65),
            gas = scaledValue(gasNoise, 350, 900)
        )
    }

    fun nextEvent(buildingId: String, measurement: Measurement): Event {
        val eventName = when {
            measurement.brightness < 350 -> "brightnessLow"
            measurement.temperature > 26 -> "temperatureHigh"
            measurement.temperature < 18 -> "temperatureLow"
            measurement.humidity > 60 -> "humidityHigh"
            measurement.gas > 750 -> "co2TooHigh"
            // Gleichmaessiges Rauschen sorgt dafuer, dass sich Tuerevents schrittweise aendern
            // und nicht jede Sekunde zufaellig hin- und herspringen.
            eventNoise.sample(tick / 8.0) > 0.2 -> "doorOpened"
            else -> "doorClosed"
        }

        val data = when (eventName) {
            "doorOpened", "doorClosed" -> "frontdoor"
            "brightnessLow" -> "lightsOn"
            else -> null
        }

        return Event(
            id = ObjectId(),
            buildingId = buildingId,
            eventName = eventName,
            data = data
        )
    }

    private fun scaledValue(noise: SmoothNoise, min: Int, max: Int): Int {
        val normalized = (noise.sample(tick / 6.0) + 1.0) / 2.0
        return (min + normalized * (max - min)).roundToInt()
    }
}
