class DistanceGrid(rowCount: Int, colCount: Int) : Grid(rowCount, colCount) {

    var distances: Distances? = null

    override fun contentsOf(cell: Cell): String {
        val distance = distances?.get(cell)
        return distance?.toString(36) ?: super.contentsOf(cell)
    }
}