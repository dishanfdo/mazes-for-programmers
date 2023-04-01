package demos

import algorithms.RecursiveBacktracker
import models.TriangleGrid
import saveAsPng

fun main() {
    val grid = TriangleGrid(10, 17)
    RecursiveBacktracker.on(grid)

    grid.toImage().saveAsPng("./mazes/delta.png")
}