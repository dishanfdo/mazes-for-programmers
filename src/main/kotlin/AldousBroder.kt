class AldousBroder {
    companion object {
        fun on(grid: Grid) {
            var cell = grid.randomCell()
            var unvisited = grid.size - 1

            fun Cell.isNotVisited() = links.isEmpty()

            while (unvisited > 0) {
                val neighbour = cell.neighbours.random()
                if (neighbour.isNotVisited()) {
                    cell.link(neighbour)
                    unvisited--
                }
                cell = neighbour
            }
        }
    }
}