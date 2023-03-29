package algorithms

import models.Cell
import models.Grid
import kotlin.random.Random

class Sidewinder {
    companion object : Algorithm {
        override val name = "Sidewinder"

        private val random = Random(System.currentTimeMillis())

        override fun on(grid: Grid) {
            val run = mutableListOf<Cell>()

            for (row in grid.rows) {
                for (cell in row) {
                    cell ?: error("Null cells not supported in Sidewinder")
                    run.add(cell)

                    val isAtEastBoundary = cell.east == null
                    val isAtNorthBoundary = cell.north == null

                    val shouldCloseOut = isAtEastBoundary || (!isAtNorthBoundary && random.nextBoolean())

                    if (shouldCloseOut) {
                        val member = run.random()
                        val memberNorth = member.north
                        if (memberNorth != null) {
                            member.link(memberNorth)
                        }
                        run.clear()
                    } else {
                        val cellEast = cell.east
                        if (cellEast != null) {
                            cell.link(cellEast)
                        }
                    }
                }
            }
        }
    }
}