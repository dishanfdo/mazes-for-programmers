package demos

import models.Grid
import algorithms.RecursiveBacktracker
import saveAsPng

fun main() {
    val grid = Grid(20, 20)
    RecursiveBacktracker.on(grid)
    grid.braid(0.5)

    grid.toImage().saveAsPng("./mazes/recursive_backtracker.png")
    grid.toImage(insetFraction = 0.1).saveAsPng("./mazes/recursive_backtracker_inset.png")
}