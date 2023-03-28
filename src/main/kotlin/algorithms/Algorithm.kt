package algorithms

import models.Grid

interface Algorithm {
    fun on(grid: Grid)
}