package demos

import algorithms.SimplifiedPrims
import algorithms.TruePrims
import models.Grid
import saveAsPng

fun main() {
    val simplePrimsGrid = Grid(20, 20)
    SimplifiedPrims.on(simplePrimsGrid)

    val truePrimsGrid = Grid(20, 20)
    TruePrims.on(truePrimsGrid)

    simplePrimsGrid.toImage().saveAsPng("./mazes/prims-simple.png")
    truePrimsGrid.toImage().saveAsPng("./mazes/prims-true.png")
}
