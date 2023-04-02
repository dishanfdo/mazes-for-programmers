package demos

import algorithms.RecursiveBacktracker
import models.WeightedCell
import models.WeightedGrid
import saveAsPng

fun main() {
    val grid = WeightedGrid(10, 10)
    RecursiveBacktracker.on(grid)

    grid.braid(0.5)
    val start = grid[0, 0] ?: error("Start cell cannot be null")
    val finish = grid[grid.rowCount - 1, grid.colCount - 1] ?: error("Finish cell cannot be null")

    grid.distances = start.distances().pathTo(finish)
    grid.toImage().saveAsPng("./mazes/original.png")
    println("Saved to original.png")

    val lava = grid.distances!!.cells.random() as WeightedCell
    lava.weight = 50

    grid.distances = start.distances().pathTo(finish)
    grid.toImage().saveAsPng("./mazes/rerouted.png")
    println("Saved to rerouted.png")
}