package demos

import algorithms.RecursiveDivision
import models.Grid
import saveAsPng

fun main() {
    val grid = Grid(20, 20)
    RecursiveDivision.on(grid)

    grid.toImage().saveAsPng("./mazes/recursive_division.png")
}