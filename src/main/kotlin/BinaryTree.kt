class BinaryTree {

    companion object {
        fun on(grid: Grid) {
            for (cell in grid.cells) {
                val cellToLink = buildList {
                    addIfNotNull(cell.north)
                    addIfNotNull(cell.east)
                }.random()
                cell.link(cellToLink)
            }
        }
    }
}