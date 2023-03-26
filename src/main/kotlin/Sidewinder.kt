import kotlin.random.Random

class Sidewinder {
    companion object {
        private val random = Random(System.currentTimeMillis())

        fun on(grid: Grid) {
            val run = mutableListOf<Cell>()

            for (row in grid.rows) {
                for (cell in row) {
                    run.add(cell)

                    val isAtEastBoundary = cell.east == null
                    val isAtNorthBoundary = cell.north == null

                    val shouldCloseOut = isAtEastBoundary || (!isAtNorthBoundary && random.nextBoolean())

                    if (shouldCloseOut) {
                        val member = run.random()
                        val memberNorth = member.north
                        if (memberNorth != null) {
                            member.link(memberNorth)
                        }
                        run.clear()
                    } else {
                        val cellEast = cell.east
                        if (cellEast != null) {
                            cell.link(cellEast)
                        }
                    }
                }
            }
        }
    }
}