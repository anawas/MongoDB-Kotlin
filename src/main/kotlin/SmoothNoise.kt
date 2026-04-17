import kotlin.math.floor
import kotlin.random.Random

/**
 * Die Klasse erzeugt eine "sanfte" Sequenz von Zufallszahlen.
 * Nutzt man die Methoden der Random-Klasse, springen die Datenwerte
 * bei jedem Aufruf stark hin und her:
 * 10, -5, 11, 12, -10, 0, 1, -5, 15, ...
 * Das ist die Idee von Zufallszahlen.
 * Messwerte tun dies aber nicht. Sie ändern sich in kleinen Schritten:
 * 10, 9, 10, 11, 10, 9, 8, 8, 8, 9, 10, 9, ...
 *
 * Diese Klasse orientiert sich an einem Algorithmus, welcher
 * "Perlin-Noise" genannt wird (nach seinem Erfinder Ken Perlin,1983).
 * Optional kann ein Seed gesetzt werden, damit die erzeugte Sequenz
 * reproduzierbar bleibt.
 */
internal class SmoothNoise(private val seed: Int = Random.nextInt()) {
    fun sample(position: Double): Double {
        val base = floor(position).toInt()
        val fraction = position - base
        val left = pseudoRandom(base)
        val right = pseudoRandom(base + 1)
        val smoothed = fraction * fraction * fraction * (fraction * (fraction * 6 - 15) + 10)
        return left + (right - left) * smoothed
    }

    // Diese Methode liefert fuer jede ganzzahlige Position einen stabilen
    // pseudozufaelligen Stuetzwert, damit sample() zwischen zwei festen
    // Nachbarwerten interpolieren kann.
    private fun pseudoRandom(x: Int): Double {
        var value = x * 374761393 + seed * 668265263
        value = (value xor (value shr 13)) * 1274126177
        value = value xor (value shr 16)
        val normalized = (value and Int.MAX_VALUE).toDouble() / Int.MAX_VALUE.toDouble()
        return normalized * 2.0 - 1.0
    }
}
