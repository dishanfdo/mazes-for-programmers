package demos

import models.ColoredGrid
import algorithms.AldousBroder
import saveAsPng

fun main() {
    repeat(6) { n ->
        val grid = ColoredGrid(20, 20)
        AldousBroder.on(grid)

        val middle = grid[grid.rowCount / 2, grid.colCount / 2] ?: error("middle cell cannot be null")
        grid.distances = middle.distances()

        grid.toImage().saveAsPng("./mazes/aldous_broder_$n.png")
    }
}