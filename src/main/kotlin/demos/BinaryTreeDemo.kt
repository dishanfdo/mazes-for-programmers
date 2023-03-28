package demos

import models.Grid
import algorithms.BinaryTree
import saveAsPng

fun main() {
    repeat(1) {
        val grid = Grid(20, 20)
        BinaryTree.on(grid)
        println(grid)
        grid.toImage().saveAsPng("./mazes/binary_tree.png")
    }
}