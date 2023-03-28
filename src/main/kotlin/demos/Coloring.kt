package demos

import models.ColoredGrid
import algorithms.BinaryTree
import saveAsPng

fun main() {
    val grid = ColoredGrid(25, 25)
    BinaryTree.on(grid)

    val start = grid[grid.rowCount / 2, grid.colCount / 2] ?: error("Start cell cannot be null")
    grid.distances = start.distances()

    grid.toImage().saveAsPng("./mazes/colorized.png")
}