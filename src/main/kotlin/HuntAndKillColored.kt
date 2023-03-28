fun main() {
    repeat(6) { n ->
        val grid = ColoredGrid(20, 20)
        HuntAndKill.on(grid)

        val middle = grid[grid.rowCount / 2, grid.colCount / 2] ?: error("middle cell cannot be null")
        grid.distances = middle.distances()

        grid.toImage().saveAsPng("./mazes/hunt_and_kill_$n.png")
    }
}