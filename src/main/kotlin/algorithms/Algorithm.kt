package algorithms

import models.Grid

interface Algorithm {
    val name: String
    fun on(grid: Grid)
}