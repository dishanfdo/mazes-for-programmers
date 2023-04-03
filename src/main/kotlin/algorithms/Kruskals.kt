package algorithms

import models.Cell
import models.Grid

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
        }

        override val name = "Kruskals"

        override fun on(grid: Grid) {
            val state = State(grid)
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