class Wilsons {
    companion object : Algorithm {
        override fun on(grid: Grid) {
            val unvisited = grid.cells.toMutableSet()
            unvisited.removeRandomElement()

            fun loopErasedPath(from: Cell): List<Cell> {
                var cell = from
                val path = mutableListOf(cell)
                while (cell in unvisited) {
                    cell = cell.neighbours.random()
                    val position = path.indexOf(cell)
                    if (position == -1) {
                        path.add(cell)
                    } else {
                        path.removeAllAfter(position)
                    }
                }
                return path
            }

            fun carvePassage(path: List<Cell>) {
                path.zipWithNext().forEach { (current, next) ->
                    current.link(next)
                    unvisited.remove(current)
                }
            }

            while (unvisited.isNotEmpty()) {
                val cell = unvisited.random()
                val path = loopErasedPath(from = cell)
                carvePassage(path)
            }
        }


    }
}