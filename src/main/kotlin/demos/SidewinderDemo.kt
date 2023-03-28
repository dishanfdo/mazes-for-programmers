package demos

import models.Grid
import algorithms.Sidewinder
import saveAsPng

fun main() {
    repeat(1) {
        val grid = Grid(20, 20)
        Sidewinder.on(grid)
        println(grid)
        grid.toImage().saveAsPng("./mazes/sidewinder.png")
    }
}