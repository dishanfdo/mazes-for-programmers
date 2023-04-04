package algorithms

import models.Cell
import models.Grid
import java.util.PriorityQueue

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

class TruePrims {
    companion object : Algorithm {
        override val name = "TruePrims"

        override fun on(grid: Grid) {
            val start = grid.randomCell()
            on(grid, start)
        }

        fun on(grid: Grid, start: Cell) {
            val costs = mutableMapOf<Cell, Int>().apply {
                for (cell in grid.cells) {
                    this[cell] = (0 until 100).random()
                }
            }

            val active = PriorityQueue(compareBy(costs::get)).apply {
                offer(start)
            }

            while (active.isNotEmpty()) {
                val cell = active.peek()
                val neighbour = cell
                    .neighbours.filter { it.links.isEmpty() }
                    .minByOrNull { c -> costs[c]!! }

                if (neighbour != null) {
                    cell.link(neighbour)
                    active.offer(neighbour)
                } else {
                    active.remove(cell)
                }
            }
        }
    }
}