package demos

import algorithms.RecursiveBacktracker
import models.WeaveGrid
import saveAsPng

fun main() {
    val grid = WeaveGrid(20, 20)
    RecursiveBacktracker.on(grid)

    grid.toImage().saveAsPng("./mazes/weave.png")
}