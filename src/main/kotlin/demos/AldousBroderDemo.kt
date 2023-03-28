package demos

import models.Grid
import algorithms.AldousBroder
import saveAsPng

fun main() {
    val grid = Grid(20, 20)
    AldousBroder.on(grid)

    grid.toImage().saveAsPng("./mazes/aldous_broder.png")
}