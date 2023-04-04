package algorithms

import models.Cell
import models.Grid

class GrowingTree {
    companion object : Algorithm {
        override val name = "GrowingTree"

        override fun on(grid: Grid) {
            on(grid.randomCell()) { cells -> cells.random() }
        }

        fun on(grid: Grid, cellSelector: (List<Cell>) -> Cell) {
            on(grid.randomCell(), cellSelector)
        }

        fun on(start: Cell, cellSelector: (List<Cell>) -> Cell) {
            val active = mutableListOf<Cell>().apply { add(start) }
            while (active.isNotEmpty()) {
                val cell = cellSelector(active)
                val neighbour = cell
                    .neighbours.filter { it.links.isEmpty() }
                    .randomOrNull()

                if (neighbour != null) {
                    cell.link(neighbour)
                    active.add(neighbour)
                } else {
                    active.remove(cell)
                }
            }
        }

    }
}