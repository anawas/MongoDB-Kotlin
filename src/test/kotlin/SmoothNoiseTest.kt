import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SmoothNoiseTest {

    @Test
    fun `same seed generates the same values`() {
        val first = SmoothNoise(seed = 1234)
        val second = SmoothNoise(seed = 1234)
        val positions = listOf(0.0, 0.25, 0.5, 1.0, 2.75, 10.5)

        positions.forEach { position ->
            assertEquals(first.sample(position), second.sample(position), 1e-12)
        }
    }

    @Test
    fun `neighboring samples change smoothly`() {
        val noise = SmoothNoise(seed = 1234)
        var previous = noise.sample(0.0)

        for (step in 1..100) {
            val current = noise.sample(step / 10.0)
            assertTrue(abs(current - previous) < 0.5, "Zu grosser Sprung zwischen zwei Nachbarwerten")
            previous = current
        }
    }
}
