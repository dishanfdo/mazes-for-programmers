package models

class CylinderGrid private constructor(rowCount: Int, colCount: Int) : Grid(rowCount, colCount) {
    companion object {
        operator fun invoke(rowCount: Int, colCount: Int): CylinderGrid {
            return CylinderGrid(rowCount, colCount).apply { initGrid() }
        }
    }

    override fun get(row: Int, col: Int): Cell? {
        if (row !in 0 until rowCount) return null
        val column = col.mod(grid[row].size)
        return grid[row][column]
    }
}