package demos

import models.Grid
import algorithms.RecursiveBacktracker
import saveAsPng

fun main() {
    val grid = Grid(20, 20)
    RecursiveBacktracker.on(grid)

    grid.toImage().saveAsPng("./mazes/recursive_backtracker.png")
}