import java.awt.Color
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class ColoredGrid(rowCount: Int, colCount: Int) : Grid(rowCount, colCount) {
    private var maxDist: Int? = 0

    var distances: Distances? by Delegates.observable(null) { _, _, _ ->
        maxDist = distances?.max()?.second
    }

    override fun backgroundColorOf(cell: Cell): Color? {
        val maxDist = maxDist ?: return null
        val distance = distances?.get(cell) ?: return null
        val intensity = (maxDist - distance).toFloat() / maxDist
        val dark = (255 * intensity).roundToInt()
        val bright = 128 + (127 * intensity).roundToInt()
        return Color(dark, bright, dark)
    }
}