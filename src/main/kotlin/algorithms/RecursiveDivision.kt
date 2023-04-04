package algorithms

import models.Grid
import random

class RecursiveDivision {
    companion object : Algorithm {
        override val name = "RecursiveDivision"

        override fun on(grid: Grid) {
            for (cell in grid.cells) {
                cell.neighbours.forEach { n -> cell.link(n, false) }
            }
            grid.divide(0, 0, grid.rowCount, grid.colCount)
        }

        private fun Grid.divide(row: Int, column: Int, height: Int, width: Int) {
            if (height <= 1 || width <= 1 || height < 5 && width < 5 && random(4) == 0) return

            if (height > width) {
                divideHorizontally(row, column, height, width)
            } else {
                divideVertically(row, column, height, width)
            }
        }

        private fun Grid.divideHorizontally(row: Int, column: Int, height: Int, width: Int) {
            val divideSouthOf = random(height - 1)
            val passageAt = random(width)
            repeat(width) { x ->
                if (x == passageAt) return@repeat

                val cell = this[row + divideSouthOf, column + x]
                cell!!.unlink(cell.south!!)
            }

            divide(row, column, divideSouthOf + 1, width)
            divide(row + divideSouthOf + 1, column, height - divideSouthOf - 1, width)
        }

        private fun Grid.divideVertically(row: Int, column: Int, height: Int, width: Int) {
            val divideEastOf = random(width - 1)
            val passageAt = random(height)
            repeat(height) { y ->
                if (y == passageAt) return@repeat

                val cell = this[row + y, column + divideEastOf]
                cell!!.unlink(cell.east!!)
            }

            divide(row, column, height, divideEastOf + 1)
            divide(row, column + divideEastOf + 1, height, width - divideEastOf - 1)
        }
    }
}