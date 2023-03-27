fun main() {
    val grid = DistanceGrid(5, 5)
    BinaryTree.on(grid)

    val start = grid[0, 0] ?: error("Start cell cannot be null")
    val distances = start.distances()

    grid.distances = distances
    println(grid)

    println("path from northwest corner to southwest corner:")
    val goal = grid[grid.rowCount - 1, 0] ?: error("Goal cell cannot be null")
    grid.distances = distances.pathTo(goal)
    println(grid)
}