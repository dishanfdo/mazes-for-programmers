package algorithms

import models.Cell
import models.Grid
import models.OverCell
import models.WeaveGrid
import kotlin.random.Random

class Kruskals {
    companion object : Algorithm {
        class State(val grid: Grid) {
            val neighbours = mutableListOf<Pair<Cell, Cell>>()
            private val setForCell = mutableMapOf<Cell, Int>()
            private val cellsInSet = mutableMapOf<Int, MutableList<Cell>>()

            init {
                for (cell in grid.cells) {
                    val set = setForCell.size

                    setForCell[cell] = set
                    cellsInSet[set] = mutableListOf(cell)

                    val south = cell.south
                    if (south != null) {
                        neighbours.add(cell to south)
                    }

                    val east = cell.east
                    if (east != null) {
                        neighbours.add(cell to east)
                    }
                }
            }

            fun canMerge(left: Cell, right: Cell): Boolean = setForCell[left] != setForCell[right]

            fun merge(left: Cell, right: Cell) {
                left.link(right)

                val winnerSet = setForCell[left] ?: error("WinnerSet cannot be null")
                val loserSet = setForCell[right]
                val losers = cellsInSet[loserSet] ?: listOf(right)

                val winners = cellsInSet[winnerSet] ?: error("winners cannot be null")
                for (loser in losers) {
                    winners.add(loser)
                    setForCell[loser] = winnerSet
                }

                cellsInSet.remove(loserSet)
            }

            fun addCrossing(cell: Cell): Boolean {
                if (grid !is WeaveGrid) return false
                if (cell.links.isNotEmpty()) return false

                val east = cell.east ?: return false
                val west = cell.west ?: return false
                val north = cell.north ?: return false
                val south = cell.south ?: return false
                if (!canMerge(east, west)) return false
                if (!canMerge(north, south)) return false

                neighbours.removeIf { (left, right) -> left == cell || right == cell }

                val rand = Random(System.currentTimeMillis())
                if (rand.nextBoolean()) {
                    merge(west, cell)
                    merge(cell, east)

                    grid.tunnelUnder(cell as OverCell)
                    merge(north, north.south!!)
                    merge(south, south.north!!)
                } else {
                    merge(north, cell)
                    merge(cell, south)

                    grid.tunnelUnder(cell as OverCell)
                    merge(west, west.east!!)
                    merge(east, east.west!!)
                }

                return true
            }
        }

        override val name = "Kruskals"

        override fun on(grid: Grid) {
            val state = State(grid)
            on(state)
        }

        fun on(state: State) {
            val neighbours = state.neighbours.apply { shuffle() }

            while (neighbours.isNotEmpty()) {
                val (left, right) = neighbours.removeFirst()
                if (state.canMerge(left, right)) {
                    state.merge(left, right)
                }
            }
        }
    }
}