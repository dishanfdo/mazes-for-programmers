package demos

import models.PolarGrid
import saveAsPng

fun main() {
    val grid = PolarGrid(8)

    grid.toImage().saveAsPng("./mazes/polar.png")
}