package models

import java.awt.Color
import kotlin.properties.Delegates

class WeightedGrid private constructor(rowCount: Int, colCount: Int) : Grid(rowCount, colCount) {
    companion object {
        operator fun invoke(rowCount: Int, colCount: Int): WeightedGrid {
            return WeightedGrid(rowCount, colCount).apply { initGrid() }
        }
    }

    private var maxDist: Int? = 0

    var distances: Distances? by Delegates.observable(null) { _, _, _ ->
        maxDist = distances?.max()?.second
    }

    override fun prepareGrid(): List<List<Cell?>> {
        return List(rowCount) { row ->
            List(colCount) { col ->
                WeightedCell(row, col)
            }
        }
    }

    override fun backgroundColorOf(cell: Cell): Color? {
        cell as WeightedCell
        if (cell.weight > 1) {
            return Color.red
        }

        val maxDist = maxDist ?: return null
        val distance = distances?.get(cell) ?: return null
        val intensity = (64 + 191 * (maxDist - distance).toFloat() / maxDist).toInt()
        return Color(intensity, intensity, 0)
    }

}