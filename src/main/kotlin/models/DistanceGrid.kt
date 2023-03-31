package models

class DistanceGrid private constructor(rowCount: Int, colCount: Int) : Grid(rowCount, colCount) {
    companion object {
        operator fun invoke(rowCount: Int, colCount: Int): DistanceGrid {
            return DistanceGrid(rowCount, colCount).apply { initGrid() }
        }
    }


    var distances: Distances? = null

    override fun contentsOf(cell: Cell): String {
        val distance = distances?.get(cell)
        return distance?.toString(36) ?: super.contentsOf(cell)
    }
}