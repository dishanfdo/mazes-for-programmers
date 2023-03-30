package demos

import algorithms.RecursiveBacktracker
import models.Mask
import models.MaskedGrid
import saveAsPng

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        error("Please specify a PNG image to use as a template")
    }

    val filePath = args[0]
    val mask = Mask.fromPng(filePath)
    val grid = MaskedGrid(mask)
    RecursiveBacktracker.on(grid)

    grid.toImage(cellSize = 5).saveAsPng("./mazes/masked.png")
}