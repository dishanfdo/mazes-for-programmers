package demos

import algorithms.RecursiveBacktracker
import models.CylinderGrid
import saveAsPng

fun main() {
    val grid = CylinderGrid(7, 16)
    RecursiveBacktracker.on(grid)

    grid.toImage().saveAsPng("./mazes/cylinder.png")
}