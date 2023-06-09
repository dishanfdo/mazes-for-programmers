package demos

import models.Grid
import algorithms.HuntAndKill
import saveAsPng

fun main() {
    val grid = Grid(20, 20)
    HuntAndKill.on(grid)

    grid.toImage().saveAsPng("./mazes/hunt_and_kill.png")
}