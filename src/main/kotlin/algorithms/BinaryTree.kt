package algorithms

import models.Grid
import addIfNotNull

class BinaryTree {
    companion object : Algorithm {
        override val name = "BinaryTree"

        override fun on(grid: Grid) {
            for (cell in grid.cells) {
                val cellToLink = buildList {
                    addIfNotNull(cell.north)
                    addIfNotNull(cell.east)
                }.randomOrNull()

                if (cellToLink != null) {
                    cell.link(cellToLink)
                }
            }
        }
    }
}