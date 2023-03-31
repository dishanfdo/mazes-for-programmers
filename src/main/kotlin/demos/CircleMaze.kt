package demos

import algorithms.RecursiveBacktracker
import models.PolarGrid
import saveAsPng

fun main() {
    val grid = PolarGrid(rowCount = 20)
    RecursiveBacktracker.on(grid)

    grid.toImage().saveAsPng("./mazes/circle_maze.png")
}