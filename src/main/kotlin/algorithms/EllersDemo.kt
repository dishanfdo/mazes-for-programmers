package algorithms

import models.Grid
import saveAsPng

fun main() {
    val grid = Grid(20, 20)
    Ellers.on(grid)

    grid.toImage().saveAsPng("./mazes/ellers.png")
    println("Saved to ellers.png")
}