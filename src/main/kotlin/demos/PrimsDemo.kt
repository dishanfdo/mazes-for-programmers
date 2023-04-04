package demos

import algorithms.SimplifiedPrims
import models.Grid
import saveAsPng

fun main() {
    val grid = Grid(20, 20)
    SimplifiedPrims.on(grid)

    grid.toImage().saveAsPng("./mazes/prims-simple.png")
}