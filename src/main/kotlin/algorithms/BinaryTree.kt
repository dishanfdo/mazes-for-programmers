package algorithms

import models.Grid
import addIfNotNull

class BinaryTree {
    companion object : Algorithm {
        override val name = "BinaryTree"

        override fun on(grid: Grid) {
            for (cell in grid.cells) {
                val cellToLink = buildList {
                    val north = grid[cell.row - 1, cell.column]
                    addIfNotNull(north)
                    val east = grid[cell.row, cell.column + 1]
                    addIfNotNull(east)
                }.randomOrNull()

                if (cellToLink != null) {
                    cell.link(cellToLink)
                }
            }
        }
    }
}