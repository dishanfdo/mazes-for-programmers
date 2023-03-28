package algorithms

import models.Cell
import models.Grid
import java.util.Stack

class RecursiveBacktracker {
    companion object : Algorithm {
        override val name = "RecursiveBacktracker"

        override fun on(grid: Grid) {
            val start = grid.randomCell()
            on(start)
        }

        private fun on(start: Cell) {
            fun Cell.isVisited() = links.isNotEmpty()
            fun Cell.isNotVisited() = !isVisited()
            fun Cell.unvisitedNeighbours() = neighbours.filter { it.isNotVisited() }

            val stack = Stack<Cell>()
            stack.push(start)

            while (stack.isNotEmpty()) {
                val current = stack.peek()
                val unvisitedNeighbours = current.unvisitedNeighbours()
                if (unvisitedNeighbours.isEmpty()) {
                    stack.pop()
                } else {
                    val neighbour = unvisitedNeighbours.random()
                    current.link(neighbour)
                    stack.push(neighbour)
                }
            }
        }
    }
}