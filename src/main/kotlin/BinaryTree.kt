class BinaryTree {

    companion object {
        fun on(grid: Grid) {
            for (cell in grid.cells) {
                val cellToLink = buildList {
                    addIfNotNull(cell.north)
                    addIfNotNull(cell.east)
                }.randomOrNull()

                if (cellToLink != null) {
                    cell.link(cellToLink)
                }
            }
        }
    }
}