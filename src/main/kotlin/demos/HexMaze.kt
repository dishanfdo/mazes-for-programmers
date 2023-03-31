package demos

import algorithms.RecursiveBacktracker
import models.HexGrid
import saveAsPng

fun main() {
    val grid = HexGrid(10, 10)
    RecursiveBacktracker.on(grid)

    grid.toImage().saveAsPng("./mazes/hex.png")
}