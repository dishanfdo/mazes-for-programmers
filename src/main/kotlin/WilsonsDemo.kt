fun main() {
    val grid = Grid(20 ,20)
    Wilsons.on(grid)

    grid.toImage().saveAsPng("./mazes/wilsons.png")
}