package demos

import algorithms.GrowingTree
import models.Grid
import saveAsPng

fun main() {
    fun save(grid: Grid, filePath: String) {
        grid.toImage().saveAsPng(filePath)
        println("saved to $filePath")
    }

    var grid = Grid(20, 20)
    GrowingTree.on(grid) { cells -> cells.random() }
    save(grid, "./mazes/growing-tree-random.png")

    grid = Grid(20, 20)
    GrowingTree.on(grid) { cells -> cells.last() }
    save(grid, "./mazes/growing-tree-last.png")

    grid = Grid(20, 20)
    GrowingTree.on(grid) { cells ->
        if ( (0 .. 1).random() == 0) cells.last() else cells.random()
    }
    save(grid, "./mazes/growing-tree-mix.png")
}