package demos

import algorithms.RecursiveBacktracker
import models.Grid

fun main() {
    val grid = Grid(5, 5)

    // orphan the cell in the northwest corner...
    grid[0, 0]?.apply {
        east?.west = null
        south?.north = null
    }

    // ...and the one in the southeast corner
    grid[4, 4]?.apply {
        west?.east = null
        north?.south = null
    }

    RecursiveBacktracker.on(start = grid[1, 1]!!)

    println(grid)
}