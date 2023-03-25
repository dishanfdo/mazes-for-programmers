fun main() {
    repeat(4) {
        val grid = Grid(4, 4)
        Sidewinder.on(grid)
        println(grid)
    }
}