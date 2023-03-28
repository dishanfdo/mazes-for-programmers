package algorithms

import models.Cell
import models.Grid

class HuntAndKill {
    companion object : Algorithm {
        override fun on(grid: Grid) {
            fun Cell.isVisited() = links.isNotEmpty()
            fun Cell.isNotVisited() = !isVisited()
            fun Cell.unvisitedNeighbours() = neighbours.filter { it.isNotVisited() }
            fun Cell.visitedNeighbours() = neighbours.filter { it.isVisited() }

            var current: Cell? = grid.randomCell()
            while (current != null) {
                val unvisitedNeighbours = current.unvisitedNeighbours()
                if (unvisitedNeighbours.isNotEmpty()) {
                    val neighbour = unvisitedNeighbours.random()
                    current.link(neighbour)
                    current = neighbour
                } else {
                    current = null

                    for (cell in grid.cells) {
                        val visitedNeighbours = cell.visitedNeighbours()
                        if (cell.isNotVisited() && visitedNeighbours.isNotEmpty()) {
                            current = cell
                            val neighbour = visitedNeighbours.random()
                            current.link(neighbour)
                            break
                        }
                    }
                }
            }
        }
    }
}