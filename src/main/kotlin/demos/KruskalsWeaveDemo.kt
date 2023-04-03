package demos

import addIfNotNull
import algorithms.Kruskals
import models.Cell
import models.OverCell
import models.WeaveGrid
import saveAsPng

class SimpleOverCell(row: Int, column: Int, grid: WeaveGrid) : OverCell(row, column, grid) {
    override val neighbours: List<Cell>
        get() = buildList {
            addIfNotNull(north)
            addIfNotNull(south)
            addIfNotNull(east)
            addIfNotNull(west)
        }
}

class PreconfiguredGrid private constructor(rowCount: Int, colCount: Int) : WeaveGrid(rowCount, colCount) {

    companion object {
        operator fun invoke(rowCount: Int, colCount: Int): PreconfiguredGrid {
            return PreconfiguredGrid(rowCount, colCount).apply { initGrid() }
        }
    }

    override fun prepareGrid(): List<List<Cell?>> {
        return List(rowCount) { row ->
            List(colCount) { col ->
                SimpleOverCell(row, col, this)
            }
        }
    }
}

fun main() {
    val grid = PreconfiguredGrid(20,20)
    val state = Kruskals.Companion.State(grid)

    repeat(grid.size) {
        val row = 1 + (0 .. grid.rowCount - 2).random()
        val col = 1 + (0 .. grid.colCount - 2).random()
        state.addCrossing(grid[row, col]!!)
    }

    Kruskals.on(state)
    grid.toImage(insetFraction = 0.2).saveAsPng("./mazes/kruskals_improved.png")
}