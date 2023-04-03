package demos

import algorithms.Kruskals
import models.Grid
import saveAsPng

fun main() {
    val grid = Grid(20, 20)
    Kruskals.on(grid)

    grid.toImage().saveAsPng("./mazes/kruskals.png")
}