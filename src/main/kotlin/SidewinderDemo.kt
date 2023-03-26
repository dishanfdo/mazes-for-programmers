fun main() {
    repeat(1) {
        val grid = Grid(32, 32)
        Sidewinder.on(grid)
        println(grid)
        grid.toImage().saveAsPng("./mazes/sidewinder.png")
    }
}