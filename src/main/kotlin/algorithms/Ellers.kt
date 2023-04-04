package algorithms

import models.Cell
import models.Grid

class Ellers {
    companion object : Algorithm {
        override val name = "Ellers"

        override fun on(grid: Grid) {
            var rowState = RowState()

            for (row in grid.rows) {
                val isLastRow = row[0]?.south == null
                for (cell in row) {
                    cell ?: continue
                    val westNeighbour = cell.west ?: continue

                    val set = rowState.setFor(cell)
                    val priorSet = rowState.setFor(westNeighbour)

                    val shouldLink = set != priorSet && (isLastRow || (0 .. 1).random() == 0)

                    if (shouldLink) {
                        cell.link(westNeighbour)
                        rowState.merge(priorSet, set)
                    }
                }

                if (!isLastRow) {
                    val nextRow = rowState.next()
                    for ((_, list) in rowState.sets) {
                        for ((index, cell) in list.shuffled().withIndex()) {
                            if (index == 0 || (0 until 3).random() == 0) {
                                val southNeighbour = cell.south ?: continue
                                cell.link(southNeighbour)
                                nextRow.record(rowState.setFor(cell), southNeighbour)
                            }
                        }
                    }

                    rowState = nextRow
                }
            }
        }

        class RowState(startingSet: Int = 0) {
            private val cellsInsSet = mutableMapOf<Int, MutableList<Cell>>()
            private val setForCell = mutableMapOf<Int, Int>()
            private var nextSet = startingSet

            fun record(set: Int, cell: Cell) {
                setForCell[cell.column] = set
                cellsInsSet[set] = cellsInsSet[set] ?: mutableListOf()
                cellsInsSet[set]?.add(cell)
            }

            fun setFor(cell: Cell): Int {
                if (setForCell[cell.column] == null) {
                    record(nextSet, cell)
                    nextSet++
                }
                return setForCell[cell.column]!!
            }

            fun merge(winner: Int, loser: Int) {
                for (cell in cellsInsSet[loser]!!) {
                    setForCell[cell.column] = winner
                    cellsInsSet[winner]?.add(cell)
                }

                cellsInsSet.remove(loser)
            }

            fun next(): RowState = RowState(nextSet)

            val sets: Sequence<Pair<Int, List<Cell>>> = cellsInsSet.asSequence().map { (set, cells) -> set to cells }
        }
    }
}