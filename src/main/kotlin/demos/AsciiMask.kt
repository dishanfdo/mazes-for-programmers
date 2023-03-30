package demos

import algorithms.RecursiveBacktracker
import models.Mask
import models.MaskedGrid
import saveAsPng

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        error("Please specify a text file to use as a template")
    }

    val filePath = args[0]
    val mask = Mask.fromTxt(filePath)
    val grid = MaskedGrid(mask)
    RecursiveBacktracker.on(grid)

    grid.toImage().saveAsPng("./mazes/masked.png")
}