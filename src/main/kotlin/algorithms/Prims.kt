package algorithms

import models.Cell
import models.Grid

class SimplifiedPrims {
    companion object : Algorithm {
        override val name = "SimplifiedPrims"

        override fun on(grid: Grid) {
            val start = grid.randomCell()
            on(start)
        }

        fun on(start: Cell) {
            val active = mutableListOf<Cell>()
            active.add(start)

            while (active.isNotEmpty()) {
                val cell = active.random()
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